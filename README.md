# 🛡️ Moderation Plugin

A powerful and lightweight moderation plugin for Minecraft Spigot servers, designed to support common administrative tasks like fly mode toggling, game mode switching, mute, kick, warn, and more.

## 📦 Features

- `/fly` – Toggle flight mode for yourself.
- `/gm <0|1|2|3>` – Change your own gamemode.
- `/kick <player> <reason>` – Kick a player with a reason.
- `/mute <player> <reason>` – Mute a player permanently or temporarily.
- `/warn <player> <reason>` – Send a warning to a player.
- `/unmute <player>` – Unmute a previously muted player.
- `/modhelp` – Displays all available commands and their usage.

Each command checks for specific permission nodes, making it ideal for role-based moderation.

## ⚙️ Configuration

The `config.yml` file contains all customizable messages and permission nodes. Example:

```yaml
prefix: "&8[&cModeration&8] &r"

messages:
  no-permission: "{prefix}&cYou don’t have permission to use this command."
  player-not-found: "{prefix}&cPlayer not found."
  kicked: "{prefix}&aKicked &e{player} &afor &e{reason}&a."
  muted: "{prefix}&aMuted &e{player} &afor &e{reason}&a."
  warned: "{prefix}&aWarned &e{player} &afor &e{reason}&a."
  not-muted: "{prefix}&c{player} is not muted."
  unmuted: "{prefix}&aUnmuted &e{player}&a."
  fly-enabled: "{prefix}&aFlight enabled."
  fly-disabled: "{prefix}&cFlight disabled."
  gm-usage: "{prefix}&cUsage: /gm <0|1|2|3>"
  gm-changed: "{prefix}&aYour game mode has been set to &e{mode}&a."

permission-nodes:
  kick: "moderation.kick"
  mute: "moderation.mute"
  warn: "moderation.warn"
  unmute: "moderation.unmute"
  fly: "moderation.fly"
  gamemode: "moderation.gamemode"
  help: "moderation.help"
  ````

## 🔐 Permissions

Each command requires specific permission nodes. Use a permissions plugin (like LuckPerms) to manage access.

| Command    | Permission            |
| ---------- | --------------------- |
| `/fly`     | `moderation.fly`      |
| `/gm`      | `moderation.gamemode` |
| `/kick`    | `moderation.kick`     |
| `/mute`    | `moderation.mute`     |
| `/warn`    | `moderation.warn`     |
| `/unmute`  | `moderation.unmute`   |
| `/help` | `moderation.help`     |

## 🧩 Setup

1. Place the plugin `.jar` in your `plugins/` directory.
2. Start or reload your server.
3. Configure `config.yml` to your preferences.
4. Assign permissions using a plugin like LuckPerms.

## ❓ Support

For issues or feature requests, please open an issue on the repository or contact [NextGen Development](https://discord.gg/zQRYbaXgNa).

---

Made with ❤️ for Minecraft moderation servers.