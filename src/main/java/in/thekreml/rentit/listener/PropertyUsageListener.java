package in.thekreml.rentit.listener;

import in.thekreml.rentit.constant.Constants;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.logging.Logger;

public class PropertyUsageListener implements Listener {
  private final Economy economy;
  private final Permission permissions;
  private final Logger log;

  public PropertyUsageListener(Economy economy, Permission permissions, Logger log) {
    this.economy = economy;
    this.permissions = permissions;
    this.log = log;
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
  public void onPlayerInteractEntity(final PlayerInteractEntityEvent event) {
      log.info("Handling onEntityInteract");

      final Player player = event.getPlayer();
      if (!permissions.has(player, Constants.PERMISSION_USAGE)) {
        player.sendMessage(Constants.ERROR_PERMISSION);
        event.setCancelled(true);
        return;
      }

      player.sendMessage("To use this property, you will need to pay!");
      event.setCancelled(true);
  }
}
