package in.thekreml.rentit.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Device {
  private transient Block block;
  private String worldName;
  private Vector position;
  private String ownerName;
  private Map<String, Integer> renters = new HashMap<>();

  public Location getLocation() {
    return new Location(Bukkit.getWorld(worldName), position.getX(), position.getY(), position.getZ());
  }

  public Block getBlock() {
    return block;
  }

  public void setBlock(Block block) {
    this.block = block;
  }

  public String getWorldName() {
    return worldName;
  }

  public void setWorldName(String worldName) {
    this.worldName = worldName;
  }

  public Vector getPosition() {
    return position;
  }

  public void setPosition(Vector position) {
    this.position = position;
  }

  public String getOwnerName() {
    return ownerName;
  }

  public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
  }

  public Map<String, Integer> getRenters() {
    return renters;
  }

  public void setRenters(Map<String, Integer> renters) {
    this.renters = renters;
  }
}
