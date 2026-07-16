# Extruding Recipe Format

## Field Reference

### type (required)

Must be `"createcompletelycreate:extruding"`

### blockIngredients (required)

The two side blocks adjacent to the Block Expeller (left and right, relative to facing).

```json
"blockIngredients": {
  "first": { "blocks": "minecraft:water" },
  "second": { "blocks": "minecraft:lava" }
}
```

- If both ingredients are the same block, order doesn't matter
- If different, the system tries to match each ingredient against each side
- Supports `BlockPredicate` syntax (tag, block, state properties)

### result (required)

The output item produced by the Block Expeller.

```json
"result": { "id": "minecraft:cobblestone" }
```

- Supports `ProcessingOutput` format (chance-based outputs possible)

### catalyst (optional, default: empty)

Block required **beneath** the Block Expeller (one block below). Not consumed.

```json
"catalyst": { "blocks": "minecraft:soil_soil" }
```

- Use `"blocks": "minecraft:water"` to require waterlogged blocks below
- Omit or use empty block predicate `{}` for no catalyst requirement

### requiredBonks (optional, default: 1)

Number of cycles (actuations) needed before output is produced.

```json
"requiredBonks": 3
```

- Each cycle takes `cycleTime` ticks (default 240 ticks / 12 seconds)
- Higher values = slower production

### advanced (optional, default: false)

Whether this recipe requires the **Brass Block Expeller** (advanced machine).

```json
"advanced": true
```

- `false` = can use either andesite or brass Block Expeller
- `true` = brass Block Expeller only

### consumeBlocks (optional, default: both false)

Whether side blocks are consumed (destroyed) after production.

```json
"consumeBlocks": {
  "first": true,
  "second": false
}
```

- Only works with brass (advanced) Block Expeller
- Consumed blocks are replaced with air

### requirements (optional, default: empty list)

List of conditions that must be met for the recipe to work.

```json
"requirements": [
  { "type": "min_y", "minY": 0 },
  { "type": "max_y", "maxY": 60 },
  { "type": "max_speed", "maxSpeed": 16.0 },
  { "type": "biome_tag", "biomeTag": "minecraft:is_nether" }
]
```

Available requirement types:
| Type | Fields | Description |
|------|--------|-------------|
| `min_y` | `minY` (int) | Machine must be at or above this Y level |
| `max_y` | `maxY` (int) | Machine must be at or below this Y level |
| `max_speed` | `maxSpeed` (float) | Rotational speed must be at or below this value |
| `min_speed` | `minSpeed` (float) | Rotational speed must be at or above this value |
| `biome_tag` | `biomeTag` (string) | Machine must be in a biome with this tag |
| `advanced_extruder` | `advanced` (boolean) | Requires brass Block Expeller (redundant with `advanced` field) |
| `bonks` | `bonks` (int) | Required number of cycles (redundant with `requiredBonks` field) |

---

## Example Recipes

### Simple: Cobblestone Generation

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

### With Catalyst: Basalt (requires soul soil beneath)

```json
{
  "type": "createcompletelycreate:extruding",
  "blockIngredients": {
    "first": { "blocks": "minecraft:blue_ice" },
    "second": { "blocks": "minecraft:lava" }
  },
  "result": { "id": "minecraft:basalt" },
  "catalyst": { "blocks": "minecraft:soul_soil" }
}
```

### With Requirements: Deepslate (low Y, slow speed)

```json
{
  "type": "createcompletelycreate:extruding",
  "blockIngredients": {
    "first": { "blocks": "minecraft:water" },
    "second": { "blocks": "minecraft:lava" }
  },
  "result": { "id": "minecraft:deepslate" },
  "requirements": [
    { "type": "max_y", "maxY": 0 },
    { "type": "max_speed", "maxSpeed": 16.0 }
  ]
}
```

### Advanced: Obsidian (brass Block Expeller, consumes blocks)

```json
{
  "type": "createcompletelycreate:extruding",
  "blockIngredients": {
    "first": { "blocks": "minecraft:water" },
    "second": { "blocks": "minecraft:lava" }
  },
  "result": { "id": "minecraft:obsidian" },
  "advanced": true,
  "consumeBlocks": { "first": false, "second": true },
  "catalyst": { "blocks": "minecraft:obsidian" }
}
```

### Multi-cycle: Stone (2 bonks with lava catalyst)

```json
{
  "type": "createcompletelycreate:extruding",
  "blockIngredients": {
    "first": { "blocks": "minecraft:cobblestone" },
    "second": { "blocks": "minecraft:water" }
  },
  "result": { "id": "minecraft:stone" },
  "catalyst": { "blocks": "minecraft:lava" },
  "requiredBonks": 2
}
```
