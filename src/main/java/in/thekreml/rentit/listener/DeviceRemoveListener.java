package in.thekreml.rentit.listener;

import in.thekreml.rentit.RentIt;
import in.thekreml.rentit.data.Device;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class DeviceRemoveListener implements Listener {
  private final RentIt plugin;

  public DeviceRemoveListener(RentIt plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    final Device device = plugin.getDataRegistry().findDevice(event.getBlock().getLocation());
    if (device == null) {
      plugin.getLog().finest("Did not find a device at location!");
      return;
    }

    plugin.getLog().info("Unregistering device due to it being broken!");
    plugin.getDataRegistry().getModel().getDevices().remove(device);
    plugin.getDataRegistry().save();
  }
}
