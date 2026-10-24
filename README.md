# AutoTaunt

Custom chat taunts for Minecraft **1.8.9 on Ornithe Gen2**, using **OneConfig 1.4.0**.

## Install

Run Minecraft with **Java 25** and Ornithe Gen2 / Fabric Loader 0.19.5 or newer. Install:

- [OneConfig 1.4.0 for Ornithe 1.8.9](https://modrinth.com/mod/oneconfig/version/ghWURw3V) and its required dependencies (Compose Multiplatform and Fabric Language Kotlin).
- [Ornithe Standard Libraries](https://modrinth.com/mod/osl), Gen2 version 0.21.1 or newer.
- [Pylon](https://modrinth.com/mod/pylon), version 0.1.7 or newer.
- The AutoTaunt jar from `build/libs`.

This port replaces the Forge build. It requires the Gen2 Ornithe mappings/runtime.

## Use

Open AutoTaunt in OneConfig or run `/taunts`. The default send key is **L**. The settings page lets you change the key and choose Default / All / Party / Shout / Smart chat. The taunt list spans the full width of the page and lets you add, edit, remove and drag to reorder messages. `/taunts toggle <number>` switches a message off without deleting it. Switched-off messages are hidden from the settings page and show as `[off]` in `/taunts list`.

- `/taunts help` lists commands.
- `/taunts add <text>` adds a message, including spaces.
- `/taunts list` lists numbered messages.
- `/taunts remove <number>` removes a message.
- `/taunts toggle <number>` enables or disables a message.
- `/taunts clear` removes all messages.
- `/taunts defaultTaunts` appends the original default messages.

Each distinct enabled, non-empty message is used once per random cycle. Smart chat uses `/shout` during supported Hypixel team games and `/ac` in solo games, lobbies and while spectating. Outside Hypixel it sends ordinary chat.

On first launch, the port imports taunts, chat type and supported keybinds from `OneConfig/config/autotaunt.json` (also checking `config/OneConfig/config/autotaunt.json`). The original file is preserved. Settings from the earlier custom OneConfig v1 editor also migrate automatically. OneConfig v1 manages the new settings and profiles. Unsupported legacy keybinds can be rebound in the settings page.

## Build

Set `JAVA_HOME` to a JDK 25 installation, then run:

```powershell
.\gradlew.bat build
```

On Linux/macOS use `./gradlew build`. The remapped mod and source jars are written to `build/libs`. `build` also runs the selection and chat-routing tests. Use `runClient` for the development client; the released OneConfig runtime and its dependencies are included in the development configuration.

Dependency versions are pinned in `gradle.properties`; OneConfig 1.4.0 was checked against Polyfrost's Maven metadata on October 11, 2026.
