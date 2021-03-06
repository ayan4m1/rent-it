package in.thekreml.rentit;

import com.comphenix.protocol.ProtocolLibrary;
import in.thekreml.rentit.command.RentCommand;
import in.thekreml.rentit.config.ConfigModel;
import in.thekreml.rentit.constant.Constants;
import in.thekreml.rentit.data.DataRegistry;
import in.thekreml.rentit.listener.AnvilEffectListener;
import in.thekreml.rentit.listener.DeviceRemoveListener;
import in.thekreml.rentit.listener.DeviceUsageListener;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class RentIt extends JavaPlugin {
  private ConfigModel configModel;
  private Economy economy;
  private Permission permissions;
  private Logger log;
  private DataRegistry dataRegistry;
  private boolean initialized = false;

  public Economy getEconomy() {
    return economy;
  }

  public Permission getPermissions() {
    return permissions;
  }

  public Logger getLog() {
    return log;
  }

  public DataRegistry getDataRegistry() {
    return dataRegistry;
  }

  public ConfigModel getConfigModel() {
    return configModel;
  }

  @Override
  public void onEnable() {
    configModel = ConfigModel.load(this);
    log = this.getLogger();

    final RegisteredServiceProvider<Permission> permsProvider = Bukkit.getServicesManager().getRegistration(Permission.class);
    if (permsProvider == null) {
      getServer().getPluginManager().disablePlugin(this);
      log.severe("Unable to find Permissions!");
      return;
    }

    final RegisteredServiceProvider<Economy> econProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
    if (econProvider == null) {
      getServer().getPluginManager().disablePlugin(this);
      log.severe("Unable to find Economy!");
      return;
    }

    permissions = permsProvider.getProvider();
    economy = econProvider.getProvider();
    dataRegistry = new DataRegistry(log);

    dataRegistry.load();

    Bukkit.getPluginManager().registerEvents(new DeviceUsageListener(this), this);
    Bukkit.getPluginManager().registerEvents(new DeviceRemoveListener(this), this);
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
}
