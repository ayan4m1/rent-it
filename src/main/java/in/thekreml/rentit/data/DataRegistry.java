package in.thekreml.rentit.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import in.thekreml.rentit.constant.Constants;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Logger;

public class DataRegistry {
  private final Logger log;
  private DataModel model;
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  public DataRegistry(Logger log) {
    this.log = log;
  }

  public void load() {
    try {
      Path dataPath = Paths.get(Constants.PATH_DATABASE);

      if (!Files.isDirectory(dataPath.getParent().toAbsolutePath())) {
        Files.createDirectories(dataPath.getParent().toAbsolutePath());
      }

      if (Files.exists(dataPath)) {
        log.info("Reading data model");

        final String data = String.join("", Files.readAllLines(dataPath));
        model = GSON.fromJson(data, DataModel.class);
      } else {
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
      log.info("Writing data model");
      Files.write(Paths.get(Constants.PATH_DATABASE), GSON.toJson(model).getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      log.severe(e.getMessage());
      log.severe(Arrays.toString(e.getStackTrace()));
    }
  }
}
