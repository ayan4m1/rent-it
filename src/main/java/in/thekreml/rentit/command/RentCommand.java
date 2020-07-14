package in.thekreml.rentit.command;

import in.thekreml.rentit.constant.Constants;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class RentCommand implements CommandExecutor {
  private final Economy economy;
  private final Permission permissions;
  private final Logger log;

  public RentCommand(Economy economy, Permission permissions, Logger log) {
    this.economy = economy;
    this.permissions = permissions;
    this.log = log;
  }

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    if (!(sender instanceof Player)) {
      return true;
    }

    if (!command.getLabel().equalsIgnoreCase("rent")) {
      return false;
    }

    final Player player = (Player)sender;
    if (!permissions.has(player, Constants.PERMISSION_USAGE)) {
      player.sendMessage(Constants.ERROR_PERMISSION);
      return false;
    }

    final EconomyResponse balanceResponse = economy.bankBalance(player.getName());

    log.info(String.valueOf(balanceResponse.balance));
    return true;
  }
}
