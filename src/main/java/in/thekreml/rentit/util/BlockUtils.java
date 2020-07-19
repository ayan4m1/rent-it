package in.thekreml.rentit.util;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockUtils {
  public static boolean isAnvil(Block block) {
    final Material mat = block.getBlockData().getMaterial();

    return mat == Material.ANVIL ||
        mat == Material.CHIPPED_ANVIL ||
        mat == Material.DAMAGED_ANVIL;
  }
}
