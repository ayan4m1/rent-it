package in.thekreml.rentit.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.BlockPosition;
import in.thekreml.rentit.RentIt;
import in.thekreml.rentit.data.Device;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class AnvilEffectListener extends PacketAdapter {
  private final RentIt plugin;
  private static final int ANVIL_BREAK_EFFECT = Effect.ANVIL_BREAK.getId();
  private static final int ANVIL_USE_EFFECT = Effect.ANVIL_USE.getId();

  public AnvilEffectListener(RentIt plugin) {
    super(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.WORLD_EVENT);
    this.plugin = plugin;
  }

  @Override
  public void onPacketSending(PacketEvent event) {
    final Player player = event.getPlayer();
    final StructureModifier<Integer> integers = event.getPacket().getIntegers();
    final int effectId = integers.read(0);

    if (effectId != ANVIL_BREAK_EFFECT && effectId != ANVIL_USE_EFFECT) {
      return;
    }

    final StructureModifier<BlockPosition> positions = event.getPacket().getBlockPositionModifier();
    final BlockPosition position = positions.read(0);
    final Location location = new Location(player.getWorld(), position.getX(), position.getY(), position.getZ());

    if (effectId == ANVIL_BREAK_EFFECT) {
      onAnvilBreakEffect(event, player, location);
    } else {
      onAnvilUseEffect(event, player, location);
    }
  }

  private void onAnvilUseEffect(PacketEvent event, Player player, Location location) {
    final Device device = plugin.getDataRegistry().findDevice(location);
    if (device == null) {
      return;
    }

    Integer remaining = device.getRenters().get(player.getName());
    if (remaining == null || remaining == 0) {
      plugin.getLog().warning(
          String.join("", "Player ", player.getName(), " was able to use a rented anvil while not on the list!")
      );
      event.setCancelled(true);
      // todo: try to undo whatever the player just did
      player.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
      return;
    }

    remaining--;

    if (remaining > 0) {
      device.getRenters().put(player.getName(), remaining);
    } else {
      device.getRenters().remove(player.getName());
      player.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
    }

    plugin.getDataRegistry().save();
  }

  private void onAnvilBreakEffect(PacketEvent event, Player player, Location location) {
    final Device device = plugin.getDataRegistry().findDevice(location);
    if (device == null) {
      plugin.getLog().info("Not saving an unregistered anvil from destruction!");
      return;
    }

    plugin.getLog().info("Saving a registered anvil from destruction!");

    // cancel event and reset block/inventory state
    event.setCancelled(true);

    final Block block = device.getBlock();
    if (block == null) {
      plugin.getLog().warning("Device block was null!");
      return;
    }

    final BlockState state = block.getState();
    state.update(true, true);
    player.updateInventory();
  }
}
