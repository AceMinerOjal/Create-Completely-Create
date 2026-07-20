# 📦 Create: Completely Create

A lightweight addon that adds dozens of new, intuitive Create-style recipes to enhance automation, progression, and late-game crafting.

**Create: Completely Create** extends the Create mod with a wide variety of balanced, automation-friendly recipes designed to fit seamlessly into the Create experience. Whether you're looking to automate block conversions, process rare materials, or improve resource chains, this addon gives you more tools to play with-without breaking vanilla progression.

## ✨ Features

### ⚙️ New Mechanical Crafting & Processing Recipes

Adds new recipes for many overlooked vanilla blocks and items using Create's existing machines, such as:

- Mechanical Press conversions

- Crushing Wheel processing

- Mixing (heated & superheated) recipes

- Milling alternatives

- Compact crafting chains for advanced resources

### 🧱 Building & Decorative Block Automation

Automate various block types that previously required manual crafting:

- Stairs → blocks

- Slabs → blocks

- Cobblestone variants

- Deep-slate processing

- Blackstone/tuff crushing

- Ice and packed ice conversions

### 🔥 Material & Resource Processing

Enhance progression with smarter resource recipes:

- Crimsite + lava/netherrack mixing

- Azureine splashing

- Wart milling

- More logical stone and ore handling

### 🍏 Special & Rare Items

Create-style automation for valuable items, including:

- Golden apple enhancements

## 🔧 MOD ONLY FEATURES

### Credits and Licensing

- Original Project: [Create Mechanical Extruder](https://modrinth.com/mod/create-mechanical-extruder)
- Creator: [oierbravo](https://modrinth.com/user/oierbravo)
- License: GNU Lesser General Public License (LGPL)

The NeoForge mod version adds exclusive kinetic machines and a custom recipe system on top of all the datapack recipes.

### Main Changes from the Original

- Removal of Recipes and creating better balancing
- Adding shafts to the both sides and moving catalyst to the top so that items can be extracted using chutes from bottom making it tilable
- Removal of the requirement for the [lib mod](https://modrinth.com/mod/mechanicals-lib)

### Block Expeller (Andesite)

A kinetic machine that generates blocks from two ingredients placed on its sides.

- **Stress Impact:** 4.0 SU
- Place a block on the **left** and **right** sides of the machine as ingredients
- Optionally place a **catalyst** block on top
- Set a **filter** on the front to choose the output
- Output items are exposed below for automated extraction
- Runs only **basic** extruding recipes

### Brass Block Expeller

An advanced version of the Block Expeller with higher throughput and recipe access.

- **Stress Impact:** 16.0 SU
- Runs **all** extruding recipes (basic + advanced)
- Produces **8x output** per cycle (configurable)
- Can **consume** ingredient blocks when processing advanced recipes
- Requires more complex crafting with brass components

### Extruding Recipes

A custom recipe type (`createcompletelycreate:extruding`) with built-in recipes like:

| Recipe      | Inputs           | Catalyst  | Advanced |
| ----------- | ---------------- | --------- | -------- |
| Cobblestone | Water + Lava     | -         | No       |
| Stone       | Water + Lava     | -         | No       |
| Limestone   | Honey + Lava     | -         | No       |
| Scoria      | Lava + Chocolate | -         | No       |
| Obsidian    | Water + Lava     | Obsidian  | Yes      |
| Snow Block  | Water + Water    | Ice       | Yes      |
| Basalt      | Lava + Blue Ice  | Soul Soil | No       |

### Recipe Requirements

Extruding recipes can have conditional requirements:

- **Y-level** constraints (min/max)
- **Speed** constraints (min/max kinetic speed)
- **Biome** tag matching
- **Machine tier** (andesite-only or brass-only)
- **Bonk count** (multi-cycle recipes)

### JEI Integration

Full JEI support with animated machine previews, recipe tooltips, and catalyst registration for both Block Expeller variants.

### Config Options

| Option                  | Default         | Description                             |
| ----------------------- | --------------- | --------------------------------------- |
| `cycleTime`             | 240 ticks (12s) | Duration of one extruding cycle         |
| `brassOutputMultiplier` | 8               | Output multiplier for the brass variant |

### 🎯 Designed With Balance in Mind

All recipes are crafted to be:

### 🌱 Vanilla-friendly

### ⚙️ True to Create's style

### 🔧 Reasonably gated behind proper machinery

### 📦 Fully compatible with modpacks

## 🙌 Why Use This Addon?

If you love Create and want more automation options, better resource loops, and new ways to integrate vanilla items into your factory, this addon gives you exactly that-without adding unnecessary complexity.

If you have any recommendations for any recipes, I am open to suggestions.

You can also visit Modrinth for visual details of the recipes:
https://modrinth.com/datapack/create-completely-create
