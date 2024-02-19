# rent-it

## prerequisites

The following are required to run rent-it:

 * [Paper](https://api.papermc.io/v2/projects/paper/versions/1.20.4/builds/430/downloads/paper-1.20.4-430.jar) 1.20.4 build 430+
 * [Vault](https://github.com/MilkBowl/VaultAPI) 1.7
 * [ProtocolLib](https://ci.dmulloy2.net/job/ProtocolLib//lastBuild/) build 679+
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
