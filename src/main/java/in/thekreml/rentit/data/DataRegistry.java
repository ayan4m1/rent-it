package in.thekreml.rentit.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import in.thekreml.rentit.constant.Constants;
import in.thekreml.rentit.util.BlockUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DataRegistry {
  private final Logger log;
  private DataModel model;
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  public DataRegistry(Logger log) {
    this.log = log;
  }

  public DataModel getModel() {
    return model;
  }

  public Device findDevice(Location location) {
    final List<Device> devices = getModel().getDevices().stream()
        .filter((device) -> device.getLocation().distance(location) == 0)
        .collect(Collectors.toList());

    if (devices.size() == 1) {
      return devices.get(0);
    } else {
      return null;
    }
  }

  public void load() {
    try {
      Path dataPath = Paths.get(Constants.PATH_DATABASE);

      if (!Files.isDirectory(dataPath.getParent().toAbsolutePath())) {
        Files.createDirectories(dataPath.getParent().toAbsolutePath());
      }

      if (Files.exists(dataPath)) {
        log.info("Reading data file");

        final String data = String.join("", Files.readAllLines(dataPath));
        model = GSON.fromJson(data, DataModel.class);

        if (model == null) {
          model = new DataModel();
        }

        model.getDevices().removeIf((device) -> {
          final World world = Bukkit.getWorld(device.getWorldName());
          if (world == null) {
            log.warning(String.join("", "Culled device because it is in the no-longer-extant world ", device.getWorldName()));
            return true;
          }

          final Block block = world.getBlockAt(device.getLocation());
          if (!BlockUtils.isAnvil(block)) {
            log.warning("Culled device because it is no longer an anvil!");
            return true;
          }

          device.setBlock(block);
          return false;
        });
      } else {
        log.info("Creating blank data file");

        Files.createFile(dataPath);
        model = new DataModel();
      }
    } catch (Exception e) {
      log.severe(e.getMessage());
      log.severe(Arrays.toString(e.getStackTrace()));
    }
  }

  public void save() {
    try {
      if (model == null) {
        return;
      }

      log.info("Writing data model");
      Files.write(Paths.get(Constants.PATH_DATABASE), GSON.toJson(model).getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      log.severe(e.getMessage());
      log.severe(Arrays.toString(e.getStackTrace()));
    }
  }
}
