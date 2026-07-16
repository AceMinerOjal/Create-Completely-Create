# Create: Completely Create

A [Create](https://modrinth.com/mod/create) addon for Minecraft 1.21.1 (NeoForge) that adds the **Block Expeller** — a kinetic machine that generates blocks from adjacent input blocks, similar to a generalized cobblestone generator.

[![Modrinth](https://img.shields.io/badge/Modrinth-hGAlcCDJ-green?logo=modrinth)](https://modrinth.com/mod/create-completely-create)

## Features

### Block Expeller

A kinetic machine that generates blocks based on adjacent input blocks (left, right, and below). Two tiers available:

| Tier | Name | Notes |
|------|------|-------|
| Andesite | **Block Expeller** | Basic variant |
| Brass | **Brass Block Expeller** | Multiplied output, can consume side blocks, brass-only recipes |

### Recipe-Driven Generation

All block generation is defined through data-driven JSON recipes, making it fully extensible via resource packs and data packs. Recipes support:

- **Block ingredients** — the two side blocks (left/right of the machine)
- **Catalyst** — a required block above the machine (not consumed)
- **Multiple cycles** — configurable number of actuations before output
- **Block consumption** — brass Block Expeller can consume side blocks after production
- **Requirement system** — Y-level limits, speed constraints, biome tags, and more

See [`docs/extruding_recipes.md`](docs/extruding_recipes.md) for the full recipe format reference.

### Extended Compacting Recipes

Includes 200+ additional Create compacting recipes for stairs, slabs, and walls from vanilla and modded blocks.

### JEI Integration

Full recipe category with animated machine rendering for JEI.

### Ponder Scenes

In-game tutorial animations via Create's Ponder system, teaching players how to use the Block Expeller.

## Requirements

| Dependency | Version |
|------------|---------|
| Minecraft | 1.21.1 |
| NeoForge | 21.1.219+ |
| Create | 6.0.10+ |
| Flywheel | 1.0.6+ |

## Installation

1. Install [NeoForge](https://neoforged.net/) for Minecraft 1.21.1
2. Install [Create](https://modrinth.com/mod/create) and its dependencies
3. Download the latest release from [Modrinth](https://modrinth.com/mod/create-completely-create)
4. Place the `.jar` file in your `mods/` folder

## Building from Source

```bash
git clone https://github.com/AceMinerOjal/createcompletelycreate.git
cd createcompletelycreate
./gradlew build
```

The built jar will be in `build/libs/`.

## Adding Custom Recipes

Create a JSON file in `data/<namespace>/recipe/extruding/`:

```json
{
  "type": "createcompletelycreate:extruding",
  "blockIngredients": {
    "first": { "blocks": "minecraft:water" },
    "second": { "blocks": "minecraft:lava" }
  },
  "result": { "id": "minecraft:cobblestone" }
}
```

See the [recipe documentation](docs/extruding_recipes.md) for all available fields and requirement types.

## Localization

Supported languages: English, Korean, Japanese, Russian, Simplified Chinese, Traditional Chinese.

## License

[LGPL-3.0](LICENCE) — Copyright (c) 2025 AceMinerOjal
