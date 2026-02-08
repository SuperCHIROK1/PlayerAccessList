<p align="center">
  <a href="https://github.com/SuperCHIROK1/PlayerAccessList/blob/main/README-RU.md"><b>RU</b></a>
</p>

# 🛡️ PlayerAccessList (PAL)
**PlayerAccessList** is a powerful and flexible access management system for your Minecraft server, providing whitelist and blacklist functionality.

![bStats Servers](https://img.shields.io/bstats/servers/29094)
![bStats Players](https://img.shields.io/bstats/players/29094)
![Software](https://img.shields.io/badge/Software-Paper_1.16%2B-orange?style=flat)
![Modrinth Downloads](https://img.shields.io/modrinth/dt/playeraccesslist)
![GitHub License](https://img.shields.io/github/license/SuperCHIROK1/PlayerAccessList)

![img](img.png)

## ✨ Features
* **MySQL support** — The plugin can connect to external databases.
* **MiniMessage support** — Uses the modern formatting system from Adventure API.
* **Asynchronous checks** — Most access checks are performed asynchronously.
* **Migration** — Allows copying data from the vanilla whitelist into the plugin database.
* **Logging** — Ability to log all actions to console or moderator chat.
* **Overrides system** — Configure custom access scenarios using Overrides.
* **Multi-language support** — Several localization files are built in.  
  [Suggest a translation on our Discord server](https://dsc.gg/bytepl)

## 🚀 Commands
| Command | Aliases | Description | Permission |
|-------|--------|-------------|------------|
| `/whitelist` | `/wl, /whl` | Manage the whitelist | `pal.whitelist` |
| `/blacklist` | `/bl, /bll` | Manage the blacklist | `pal.blacklist` |
| `/pal` | `/playeraccess, /playeraccesslist` | Main plugin command (reload, migration) | `pal.admin` |

## 📈 bStats Analytics
[View **PlayerAccessList on bStats**](https://bstats.org/plugin/bukkit/PlayerAccessList/29094)

![bStats](https://bstats.org/signatures/bukkit/PlayerAccessList.svg)

## ⚙️ Developer API
A short guide for developers on how to use the plugin API.

### Setup

To connect the plugin and its API to your project, download the plugin.  
Create a `libs` folder in your project, place the downloaded plugin there, and rename it to `PlayerAccessList.jar`.

Then add the plugin as a dependency.  
Replace `YOUR VERSION` with the version you downloaded.

**Maven:**
```xml
<dependencies>
    <dependency>
        <groupId>me.superchirok1</groupId>
        <artifactId>PlayerAccessList</artifactId>
        <version>YOUR VERSION</version>
        <scope>system</scope>
        <systemPath>${project.basedir}/libs/PlayerAccessList.jar</systemPath>
    </dependency>
</dependencies>
```
**Gradle:**
```
dependencies {
    implementation files('libs/PlayerAccessList.jar')
}
```

### Classes
Adding a player to the whitelist:
```java
// Get whitelist instance
Whitelist whitelist = Whitelist.getInstance();

// Add player nickname
// UUID, OfflinePlayer, and IP address are also supported
whitelist.add("SuperCHIROK1");
```

Blacklist works almost the same way:
```java
Blacklist blacklist = Blacklist.getInstance();

blacklist.add("SuperCHIROK1");
```
### Event Classes

There are several event classes available for listeners:
* `WhitelistCancelJoinEvent` - Player kicked because they are not whitelisted.
* `BlacklistCancelJoinEvent` - Player kicked because they are blacklisted.

Example (broadcasting a message when a player tries to join):
```java
public class JoinListener implements Listener {

    @EventHandler
    public void onWhitelistCancel(WhitelistCancelJoinEvent event) {

        PlayerProfile player = event.getPlayer();

        String message = ChatColor.translateAlternateColorCodes('&',
                "&cPlayer &7" + player.getName() + "&c tried to join, but was not whitelisted");

        Bukkit.broadcastMessage(message);

    }

}
```
