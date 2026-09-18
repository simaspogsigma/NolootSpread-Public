<div align="center">

# 📦 NoLootSpread

**A lightweight, high-performance Paper plugin that eliminates the notorious death loot scattering bug in Minecraft 1.21 – 1.21.1+.**

[![Minecraft](https://img.shields.io/badge/Minecraft-1.20%20to%201.21+-brightgreen?style=for-the-badge&logo=minecraft)](https://papermc.io)
[![Platform](https://img.shields.io/badge/Platform-Paper%20%7C%20Purpur%20%7C%20Spigot-blue?style=for-the-badge)](https://papermc.io)
[![Java](https://img.shields.io/badge/Java-21+-orange?style=for-the-badge&logo=openjdk)](https://adoptium.net)
[![BuiltByBit](https://img.shields.io/badge/BuiltByBit-Resource%20Page-6c5ce7?style=for-the-badge)](https://builtbybit.com/resources/nolootspread-fix-loot-drops-on-death.67014/)

<br />

[**Download on BuiltByBit**](https://builtbybit.com/resources/nolootspread-fix-loot-drops-on-death.67014/) • [**Features**](#-features) • [**How It Works**](#%EF%B8%8F-how-nolootspread-solves-it) • [**Build**](#-building-from-source)

</div>

---

> [!NOTE]
> ### 📢 Official Public Source Release
> **NoLootSpread has been active, tested, and maintained privately for a whole year!**  
> While pre-compiled releases were distributed exclusively on BuiltByBit, **the entire source code is now officially open-source and free for the community.**

---

## 💥 The 1.21 Death Drop Bug

In Minecraft versions **1.21 through 1.21.1+**, changes in vanilla death-drop mechanics caused player items to violently explode outward upon death, launching loot up to **50+ blocks away** in every direction.

### The Consequences:
* 🔥 **Lost Equipment**: Gear and valuables flung directly into lava, fire, and deep ravines.
* 🧱 **Chunk Glitches**: Items shot through walls or into inaccessible, unloaded chunks.
* ⏳ **PvP Frustration**: Instead of fighting, players spend minutes combing a 50-block perimeter trying to locate their gear.

---

## 🛡️ How NoLootSpread Solves It

NoLootSpread overrides vanilla's broken drop spread mechanics cleanly:

1. **Drop Interception**  
   Intercepts the `PlayerDeathEvent` and suppresses the chaotic vanilla scattering velocity.
2. **Safe Inventory Snapshot**  
   Copies all items from the player's main inventory, armor slots, and off-hand slot without risk of item loss.
3. **Controlled Drop Engine**  
   Smoothly spawns items in a tightly grouped stack or controlled circular radius at the death location with customizable vertical lift.
4. **Smart `keepInventory` Sync**  
   Safely coordinates with `keepInventory` gamerules and world states to prevent accidental duplication or item wipes.

---

## ✨ Features

- ⚡ **Zero Performance Impact**: Ultra-lightweight event handling with zero tick lag.
- 🎯 **Configurable Spread Radius**: Drop items in an exact single stack or a tight, realistic circle.
- 🚀 **Adjustable Pop Velocity**: Set how high items gently pop up when dropping.
- 🔄 **Hot Reload Support**: Reload settings in real-time with zero server downtime.
- 🛡️ **Failsafe System**: Gracefully protects player gear across world reloads and gamerule changes.

---

## 🎮 Commands & Permissions

| Command | Aliases | Description | Permission | Default |
| :--- | :--- | :--- | :--- | :--- |
| `/nolootspread reload` | `/nls reload` | Reloads plugin configuration in real-time | `nolootspread.reload` | `OP` |

---

## 🔨 Building from Source

### Prerequisites
- **JDK 21** or newer
- **Maven 3.8+**

### Compile
Clone the repository and compile using standard Maven:

```bash
# Clean and package the JAR
mvn clean package
```

The compiled, shaded JAR will be available in the `target/` directory:
```
target/NolootSpread-1.9.jar
```

---

## 🔗 Official Links & Resources

* 🛒 **BuiltByBit Page**: [NoLootSpread on BuiltByBit](https://builtbybit.com/resources/nolootspread-fix-loot-drops-on-death.67014/)
* 📦 **Compatible Server Software**: Paper, Purpur, Spigot (1.20 – 1.21+)

---

<div align="center">
  <sub>Maintained with ❤️ by simaspog • Free & Open Source for the Minecraft Community</sub>
</div>
