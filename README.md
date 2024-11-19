# rent-it

## prerequisites

The following are required to run rent-it:

 * One of the following versions of Paper:
   * 1.20.1
   * 1.20.4 build 430+
   * 1.20.6
   * 1.21.3 build 131+

 * [Paper](https://papermc.io/downloads) 1.20.4 build 430+ **OR** 1.21.1 build 131+
 * [Vault](https://github.com/MilkBowl/VaultAPI) 1.7.1
 * [ProtocolLib](https://github.com/dmulloy2/ProtocolLib/releases/tag/5.3.0) 5.3.0
 * A plugin that supports the Vault Permissions API
 * A plugin that supports the Vault Economy API

## usage

To use the plugin, target an anvil and use the `/rent register` command. Once registered, the behavior of the anvil
changes. In order to use it, you must target it and use the `/rent` command, which will deduct the configured amount of
money from the player's balance and grant the configured number of uses of the anvil. Then, the player can right click
on the anvil and use it as normal. Any registered anvil will be protected from breakage. To unregister an anvil, target
it and use the `/rent unregister` command.

## configuration

The following configuration options are available in `plugins/rent-it/config.yml`

 * **cost** - integer, amount of currency to deduct every time an anvil is rented
 * **usages** - integer, number of anvil usages to grant with each rental

## permissions

The following permissions are available to control access to plugin features

 * **rentit.usage** - Whether the player can use the /rent command
 * **rentit.register** - Whether the player can use the /rent register command
 * **rentit.unregister** - Whether the player can use the /rent unregister command

## development

Use `gradle assemble` to build the application in JAR format.
