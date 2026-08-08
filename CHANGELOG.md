# Changelog - Create: Completely Create

## v2.1.0

### New Recipes

#### Spout-Water (Copper Oxidation)
- Spout water onto copper to advance its oxidation stage (Exposed -> Weathered -> Oxidized)
- Minecraft: Copper Block, Cut Copper (block/slab/stairs), Chiseled Copper, Copper Bulb, Copper Door, Copper Grate, Copper Trapdoor
- Create: Copper Shingles and Copper Tiles (block/slab/stairs)

### New Features (MOD ONLY)
- In-game config screen for the server config (Create-style, accessible from the pause menu)
- Per-category recipe toggles: every NeoForge recipe is gated behind `createcompletelycreate:config_enabled` so whole categories can be disabled in-game
- Per-recipe extruding toggles for all 8 extruding recipes
- New extruding recipe: Calcite (Levitite Blend/Levitite + Lava) - requires the Aeronautics mod

### Changes
- Replaced the catnip client/server config with a server-only NeoForge ModConfigSpec
- Extruding datagen now emits per-recipe config conditions
- Datagen output moved to `build/datagen` with a `copyGeneratedExtruding` task to sync extruding recipes
- Updated pack.mcmeta to v2.1.0

---

## v2.0.0

### New Features (MOD ONLY)
- Block Expeller and Brass Block Expeller
- 7 extruding recipes (Stone, Cobblestone, Basalt, Limestone, Scoria, Obsidian, Snow Block)
- JEI integration for extruding recipes with animated previews
- Requirement system (min/max speed, min/max Y, biome tag, advanced extruder)

### New Recipes

#### Compacting
- Create stone slab variants -> Create stone blocks (Cut, Bricks, Polished Cut, Small Bricks for all 14 Create stone types)
- Create copper shingle/tile slabs -> blocks (all oxidation/waxed variants)
- Create stone stair variants -> Create stone blocks (all stone types)
- Create copper shingle/tile stairs -> blocks (all oxidation/waxed variants)

#### Mechanical Crafting
- Create stone variant stairs (Cut, Bricks, Polished Cut, Small Bricks for all stone types)
- Create copper shingle/tile stairs (all oxidation/waxed variants)

### Changes
- Updated Blackstone crushing recipe
- Wither skeleton skull chance reduced from 1% to 0.1%
- Updated pack.mcmeta to v2.0.0
- Updated pack.png

---

## v1.1

### Added
- Pressing: Stone -> Deepslate
- Compacting: Cobblestone + Lava -> Magma Block
- Crushing: Coarse Dirt -> Dirt
- Mixing (heated): Basalt + Charcoal -> Gunpowder
- Splashing: Rotten Flesh -> Leather, Magma Block -> Magma Cream
- Haunting: Crying Obsidian

---

## v1.0

### Initial Release
- Compacting: Vanilla slabs/blocks -> blocks, vanilla stairs -> blocks
- Mechanical Crafting: All vanilla stairs and trapdoors
- Crushing: Stone -> Gravel/Flint, Deepslate -> Cobbled Deepslate/Coal, Blackstone -> Tuff/Gold Nugget
- Wood Cutting: All wood types and stripped variants
- Mixing: Blue Ice, Packed Ice, Veridium
- Splashing: Prismarine -> Asurine
- Haunting: Netherrack
- Milling: Nether Wart
