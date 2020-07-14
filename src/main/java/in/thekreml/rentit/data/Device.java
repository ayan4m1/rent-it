package in.thekreml.rentit.data;

import org.bukkit.block.Block;

public class Device {
  private Block block;
  private String ownerName;

  public Block getBlock() {
    return block;
  }

  public void setBlock(Block block) {
    this.block = block;
  }

  public String getOwnerName() {
    return ownerName;
  }

  public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
  }
}
