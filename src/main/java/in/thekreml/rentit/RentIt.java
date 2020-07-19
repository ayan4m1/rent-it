package in.thekreml.rentit;

import com.comphenix.protocol.ProtocolLibrary;
import in.thekreml.rentit.command.RentCommand;
import in.thekreml.rentit.config.ConfigModel;
import in.thekreml.rentit.constant.Constants;
import in.thekreml.rentit.data.DataRegistry;
import in.thekreml.rentit.listener.AnvilEffectListener;
import in.thekreml.rentit.listener.DeviceUsageListener;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;

public class RentIt extends JavaPlugin {
  private ConfigModel config;
  private Economy economy;
  private Permission permissions;
  private Logger log;
  private DataRegistry dataRegistry;
  private boolean initialized = false;

  public Economy getEconomy() {
    return economy;
  }

  public void setEconomy(Economy economy) {
    this.economy = economy;
  }

  public Permission getPermissions() {
    return permissions;
  }

  public void setPermissions(Permission permissions) {
    this.permissions = permissions;
  }

  public Logger getLog() {
    return log;
  }

  public void setLog(Logger log) {
    this.log = log;
  }

  public DataRegistry getDataRegistry() {
    return dataRegistry;
  }

  public void setDataRegistry(DataRegistry dataRegistry) {
    this.dataRegistry = dataRegistry;
  }

  @Override
  public void onEnable() {
    config = new ConfigModel();
    log = this.getLogger();

    try {
      this.getConfig().load(Constants.PATH_CONFIG);
      config.setCost(this.getConfig().getInt("cost"));
      config.setUsages(this.getConfig().getInt("usages"));
    } catch (IOException e) {
      log.severe(String.join("" , "IOException during config read: ", e.getMessage()));
    } catch (InvalidConfigurationException e) {
      log.severe(String.join("" , "InvalidConfigurationException during config read: ", e.getMessage()));
    }

    final RegisteredServiceProvider<Permission> permsProvider = Bukkit.getServicesManager().getRegistration(Permission.class);
    if (permsProvider == null) {
      getServer().getPluginManager().disablePlugin(this);
      log.severe("Unable to find Permissions!");
      return;
    }

    final RegisteredServiceProvider<Economy> econProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
    if (econProvider == null) {
      getServer().getPluginManager().disablePlugin(this);
      log.severe("Unable to find Vault!");
      return;
    }

    permissions = permsProvider.getProvider();
    economy = econProvider.getProvider();
    dataRegistry = new DataRegistry(log);

    dataRegistry.load();

    Bukkit.getPluginManager().registerEvents(new DeviceUsageListener(this), this);
    ProtocolLibrary.getProtocolManager().addPacketListener(new AnvilEffectListener(this));

    final PluginCommand command = getCommand(Constants.COMMAND_RENT);

    if (command != null) {
      command.setExecutor(new RentCommand(this));
    }

    initialized = true;
    log.info("RentIt enabled!");
  }

  @Override
  public void onDisable() {
    if (initialized) {
      dataRegistry.save();
    }

    ProtocolLibrary.getProtocolManager().removePacketListeners(this);
    getServer().getServicesManager().unregister(this);
    Bukkit.getScheduler().cancelTasks(this);
    log.info("RentIt disabled!");
  }

  @Override
  public void saveConfig() {
    dataRegistry.save();
  }

  @Override
  public void saveDefaultConfig() {
    dataRegistry.save();
  }
}
