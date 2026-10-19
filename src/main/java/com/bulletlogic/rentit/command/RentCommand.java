package com.bulletlogic.rentit.command;

import com.bulletlogic.rentit.RentIt;
import com.bulletlogic.rentit.constant.Constants;
import com.bulletlogic.rentit.data.Device;
import com.bulletlogic.rentit.util.BlockUtils;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RentCommand implements CommandExecutor {
  private final RentIt plugin;

  public RentCommand(RentIt plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final String[] args) {
    if (!(sender instanceof Player player)) {
      return false;
    }

    if (!command.getLabel().equalsIgnoreCase(Constants.COMMAND_RENT)) {
      return false;
    }

    if (args.length == 0) {
      return onUseCommand(player, command, args);
    }

    return switch (args[0].toLowerCase()) {
      case Constants.COMMAND_REGISTER -> onRegisterCommand(player, command, args);
      case Constants.COMMAND_UNREGISTER -> onUnregisterCommand(player, command, args);
      default -> {
        player.sendMessage(Constants.ERROR_USAGE);
        yield false;
      }
    };
  }

  private boolean onUseCommand(final Player player, final Command command, final String[] args) {
    if (!plugin.getPermissions().has(player, Constants.PERMISSION_USAGE)) {
      player.sendMessage(Constants.ERROR_PERMISSION);
      return false;
    }

    final Device device = getTargetDevice(player);
    if (device == null) {
      player.sendMessage(Constants.ERROR_NOT_REGISTERED);
      return false;
    }

    if (device.getRenters().containsKey(player.getName())) {
      player.sendMessage(Constants.ERROR_ALREADY_RENTED);
      return false;
    }

    final int cost = plugin.getConfigModel().getCost();
    if (!plugin.getEconomy().has(player, cost)) {
      player.sendMessage(Constants.ERROR_INSUFFICIENT_BALANCE);
      return false;
    }

    final EconomyResponse withdrawalResponse = plugin.getEconomy().withdrawPlayer(player, cost);
    if (!withdrawalResponse.transactionSuccess()) {
      player.sendMessage(Constants.ERROR_WITHDRAWAL);
      return false;
    }

    device.getRenters().put(player.getName(), plugin.getConfigModel().getUsages());
    plugin.getDataRegistry().save();
    player.sendMessage(String.format("Paid $%d to rent this anvil!", cost));
    return true;
  }

  private boolean onRegisterCommand(final Player player, final Command command, final String[] args) {
    if (!plugin.getPermissions().has(player, Constants.PERMISSION_REGISTER)) {
      player.sendMessage(Constants.ERROR_PERMISSION);
      return false;
    }

    final Block anvil = getTargetAnvil(player);
    if (anvil == null) {
      return false;
    }

    final Device device = plugin.getDataRegistry().findDevice(anvil.getLocation());
    if (device != null) {
      player.sendMessage(Constants.ERROR_ALREADY_REGISTERED);
      return false;
    }

    final Device newDevice = new Device();
    newDevice.setOwnerName(player.getName());
    newDevice.setPosition(anvil.getLocation().toVector());
    newDevice.setWorldName(anvil.getWorld().getName());
    newDevice.setBlock(anvil);

    plugin.getDataRegistry().getModel().getDevices().add(newDevice);
    player.sendMessage(Constants.MESSAGE_REGISTER);
    return true;
  }

  private boolean onUnregisterCommand(final Player player, final Command command, final String[] args) {
    if (!plugin.getPermissions().has(player, Constants.PERMISSION_UNREGISTER)) {
      player.sendMessage(Constants.ERROR_PERMISSION);
      return false;
    }

    final Device device = getTargetDevice(player);
    if (device == null) {
      player.sendMessage(Constants.ERROR_NOT_REGISTERED);
      return false;
    }

    plugin.getDataRegistry().getModel().getDevices().remove(device);
    player.sendMessage(Constants.MESSAGE_UNREGISTER);
    return true;
  }

  private Block getTargetAnvil(final Player player) {
    final Block targetBlock = player.getTargetBlockExact(6);
    if (targetBlock == null) {
      player.sendMessage(Constants.ERROR_NO_TARGET);
      return null;
    }

    if (!BlockUtils.isAnvil(targetBlock)) {
      player.sendMessage(Constants.ERROR_INVALID_TARGET);
      return null;
    }

    return targetBlock;
  }

  private Device getTargetDevice(final Player player) {
    final Block anvil = getTargetAnvil(player);
    if (anvil == null) {
      return null;
    }

    final Device device = plugin.getDataRegistry().findDevice(anvil.getLocation());
    if (device == null) {
      return null;
    }

    device.setBlock(anvil);
    return device;
  }
}
