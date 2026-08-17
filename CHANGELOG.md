# Changelog - Create: Completely Create

## v2.2.0

### New Features (MOD ONLY)

- Three paxels gated behind Create: Quality of Life (createqol): Shadow Steel Paxel, Refined Radiance Paxel, Shadow Radiance Paxel
- Paxels are combined pickaxe + axe + shovel multitools that mine all three material types
- Abilities mirror the corresponding createqol materials and are toggled in-hand (sneak + right-click cycles the selected ability, right-click toggles it):
  - Shadow Steel Paxel: Digging (3x3x3), Tree Decapitation, Reach
  - Refined Radiance Paxel: Vein Mine, Casingifier, Smelting, Reach
  - Shadow Radiance Paxel: Digging, Vein Mine, Casingifier, Tree Decapitation, Smelting, Reach
- Reach is applied automatically via createqol's item-config screen; abilities can also be configured there
- Vein Mine, Digging and Tree Decapitation trigger on block break; Smelting transforms drops; Casingifier transforms logs into casing on use
- Shadow Radiance Paxel can be crafted via sequenced assembly from either base paxel
- If createqol is not installed, nothing is registered (optional, compile-only dependency)
- Paxels depend on createqol as a compile-only optional dependency, registered only when createqol is present
- Added startup config toggle for paxels (`compat.paxels`); requires game restart to take effect

### New Recipes

- Shadow Steel Paxel: mechanical crafting with `create:shadow_steel` materials (requires createqol)
- Refined Radiance Paxel: mechanical crafting with `create:refined_radiance` materials (requires createqol)
- Shadow Radiance Paxel: sequenced assembly from Shadow Steel or Refined Radiance Paxel + `createqol:shadow_radiance_block` (requires createqol)
- Tuff compacting: Diorite + Flint + Lava → Tuff
- Haunting: End Stone → Prismarine
- Crushing: Prismarine → Lapis Lazuli
- Superheated Mixing: Blackstone + Sandstone → End Stone

### Recipe Changes

- Block Expeller: replaced glass with iron plate in crafting recipe
- Brass Block Expeller: expanded recipe to 5x4 grid, now requires Precision Mechanism, Netherite Ingot, and Nether Star
- Blackstone crushing: removed tuff output, Wither Skeleton Skull chance reduced from 0.1% to 0.05%
- Removed Gunpowder mixing recipe (Basalt + Charcoal) and Lapis Lazuli compacting recipe from datapack
- Asurine/Veridium swap: Splashing Prismarine now yields Veridium (was Asurine); Mixing now yields Asurine (was Veridium)
- Migrated all fluid ingredients from deprecated `fluid_stack` to `neoforge:single` format (both mod and datapack)

### Changes

- Removed the custom Create-style in-game config screen (catnip `BaseConfigScreen`); server config is now edited via NeoForge's default config screen, fixing a save crash
- Extruder config restructured to per-tier: andesite and brass now have independent `cycleTime` and `stressImpact`
- Brass `outputMultiplier` max raised from 64 to 256
- Added sub-category recipe toggles (compacting_slabs, compacting_stairs, cutting_woods, mechanical_crafting_stairs, etc.)
- Added `sequenced_assembly` recipe category toggle
- Recipe conditions updated to use sub-category config paths

### Bug Fixes

- Fixed `config_enabled` recipe conditions throwing a `ClassCastException`, which broke extruding recipe toggles; toggles now work correctly on `/reload`
- Fixed a crash when saving the server config in-game (concurrent writes from the screen and the sync packet); resolved by removing the custom config screen

---

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
