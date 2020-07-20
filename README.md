# rent-it

## prerequisites

The following are required to run rent-it

 * [Paper](https://papermc.io/ci/job/Paper-1.16) 1.16 build 88+
 * [Vault](https://github.com/MilkBowl/VaultAPI) 1.7
 * [ProtocolLib](https://ci.dmulloy2.net/job/ProtocolLib/472/) build 472
 * A plugin that supports the Vault Permissions API
 * A plugin that supports the Vault Economy API

## configuration

The following configuration options are available in `plugins/rent-it/config.yml`

 * **cost** - integer, amount of currency to deduct every time an anvil is rented
 * **usages** - integer, number of anvil usages to grant with each rental

## development

Use `gradle assemble` to build the application in JAR format.
