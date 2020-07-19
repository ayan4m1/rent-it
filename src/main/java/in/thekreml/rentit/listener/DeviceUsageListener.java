package in.thekreml.rentit.listener;

import in.thekreml.rentit.RentIt;
import in.thekreml.rentit.constant.Constants;
import in.thekreml.rentit.data.Device;
import in.thekreml.rentit.util.BlockUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class DeviceUsageListener implements Listener {
  private final RentIt plugin;

  public DeviceUsageListener(RentIt plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }

    final Block clickedBlock = event.getClickedBlock();
    if (clickedBlock == null || !BlockUtils.isAnvil(clickedBlock)) {
      return;
    }

    final Location deviceLocation = event.getClickedBlock().getLocation();
    final Device device = plugin.getDataRegistry().findDevice(deviceLocation);

    if (device == null) {
      plugin.getLog().fine("Player used an unregistered anvil!");
      return;
    }

    final Player player = event.getPlayer();
    if (!plugin.getPermissions().has(player, Constants.PERMISSION_USAGE)) {
      plugin.getLog().info(String.join("", "Player ", player.getDisplayName(), " does not have permission to use anvil!"));
      player.sendMessage(Constants.ERROR_PERMISSION);
      event.setCancelled(true);
      return;
    }

    // send a usage example to the player and block their usage
    // if they are not a renter. if they are, then proceed normally.
    if (!device.getRenters().containsKey(player.getName())) {
      player.sendMessage(Constants.MESSAGE_USE);
      event.setCancelled(true);
    }
  }
}
