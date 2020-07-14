package in.thekreml.rentit;

import in.thekreml.rentit.command.RentCommand;
import in.thekreml.rentit.data.DataRegistry;
import in.thekreml.rentit.listener.PropertyUsageListener;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class RentIt extends JavaPlugin {
  private static Logger log;
  private static Permission permissions;
  private static Economy economy;
  private static DataRegistry dataRegistry;
  private static boolean initialized = false;

  @Override
  public void onEnable() {
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
      log.severe("Unable to find Vault!");
      return;
    }

    permissions = permsProvider.getProvider();
    economy = econProvider.getProvider();
    dataRegistry = new DataRegistry(log);

    dataRegistry.load();

    Bukkit.getPluginManager().registerEvents(new PropertyUsageListener(economy, permissions, log), this);

    final PluginCommand command = getCommand("rent");

    if (command != null) {
      command.setExecutor(new RentCommand(economy, permissions, log));
    }

    initialized = true;
    log.info("RentIt enabled!");
  }

  @Override
  public void onDisable() {
    dataRegistry.save();
    getServer().getServicesManager().unregister(this);
    Bukkit.getScheduler().cancelTasks(this);
    log.info("RentIt disabled!");
  }
}
