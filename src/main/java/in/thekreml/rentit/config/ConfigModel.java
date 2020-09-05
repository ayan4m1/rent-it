package in.thekreml.rentit.config;

import in.thekreml.rentit.constant.Constants;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class ConfigModel {
  private int cost;
  private int usages;

  public int getCost() {
    return cost;
  }

  public void setCost(int cost) {
    this.cost = cost;
  }

  public int getUsages() {
    return usages;
  }

  public void setUsages(int usages) {
    this.usages = usages;
  }

  public static ConfigModel load(JavaPlugin plugin) {
    final ConfigModel result = new ConfigModel();

    try {
      plugin.saveDefaultConfig();
      plugin.getConfig().load(Constants.PATH_CONFIG);

      final int cost = plugin.getConfig().getInt(Constants.CONFIG_KEY_COST);
      final int usages = plugin.getConfig().getInt(Constants.CONFIG_KEY_USAGES);

      if (cost < 0) {
        throw new InvalidConfigurationException("Cost cannot be less than zero!");
      } else if (usages < 1) {
        throw new InvalidConfigurationException("Usages cannot be less than one!");
      }

      result.setCost(cost);
      result.setUsages(usages);

      plugin.getLogger().info(
          String.join("", "Configured to charge $",
              String.valueOf(cost), " for ", String.valueOf(usages), " uses"
          )
      );
    } catch (IOException e) {
      plugin.getLogger().severe(String.join("" , "IOException during config read: ", e.getMessage()));
    } catch (InvalidConfigurationException e) {
      plugin.getLogger().severe(String.join("" , "InvalidConfigurationException during config read: ", e.getMessage()));
    }

    return result;
  }
}
