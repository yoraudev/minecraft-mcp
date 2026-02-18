# Minecraft MCP Tools

This document provides detailed specifications for all tools exposed by the Minecraft MCP server.

## `mmcp.ping`

Arguments: none.

Response:

- Text response: `pong`

## `get_player_position`

Arguments: none.

Response fields:

| Field | Type | Description |
| --- | --- | --- |
| `x` | number | Player X coordinate. |
| `y` | number | Player Y coordinate. |
| `z` | number | Player Z coordinate. |
| `yaw` | number | Horizontal look rotation. |
| `pitch` | number | Vertical look rotation. |
| `block_x` | integer | Block-aligned X position. |
| `block_y` | integer | Block-aligned Y position. |
| `block_z` | integer | Block-aligned Z position. |
| `dimension` | string | Current dimension registry id. |
| `server_address` | string | Multiplayer server address or `singleplayer`. |

## `get_player_health_hunger`

Arguments: none.

Response fields:

| Field | Type | Description |
| --- | --- | --- |
| `health` | number | Current health value. |
| `max_health` | number | Maximum health value. |
| `absorption` | number | Current absorption hearts. |
| `food_level` | integer | Hunger bar value. |
| `saturation` | number | Saturation value. |

## `get_inventory`

Arguments: none.

Response fields:

| Field | Type | Description |
| --- | --- | --- |
| `selected_slot` | integer | Currently selected hotbar slot index. |
| `item_count` | integer | Number of non-empty slots returned. |
| `items` | array | Non-empty inventory entries. |

Each `items[]` entry:

| Field | Type | Description |
| --- | --- | --- |
| `section` | string | One of `hotbar`, `main`, `armor`, `offhand`, `extra`. |
| `slot` | integer | Raw inventory slot index. |
| `item_id` | string | Item registry id, for example `minecraft:stone`. |
| `count` | integer | Stack count. |

## `get_nearby_blocks`

Arguments:

| Name | Type | Min | Max | Default | Description |
| --- | --- | --- | --- | --- | --- |
| `radius` | integer | `1` | `12` | `3` | Search radius around player block position. |
| `include_air` | boolean | - | - | `false` | Include air blocks in results. |
| `limit` | integer | `1` | `4096` | `256` | Maximum number of returned blocks. |

Response fields:

| Field | Type | Description |
| --- | --- | --- |
| `radius` | integer | Effective radius used by the tool. |
| `include_air` | boolean | Effective include air flag. |
| `count` | integer | Number of returned block entries. |
| `blocks` | array | Block entries. |

Each `blocks[]` entry:

| Field | Type | Description |
| --- | --- | --- |
| `x` | integer | Block X coordinate. |
| `y` | integer | Block Y coordinate. |
| `z` | integer | Block Z coordinate. |
| `block_id` | string | Block registry id. |

## `get_nearby_entities`

Arguments:

| Name | Type | Min | Max | Default | Description |
| --- | --- | --- | --- | --- | --- |
| `radius` | number | `1.0` | `128.0` | `32.0` | Search radius around the player. |
| `limit` | integer | `1` | `256` | `64` | Maximum number of returned entities. |
| `include_players` | boolean | - | - | `true` | Include player entities in results. |

Behavior:

- Results are sorted by nearest first.
- Returned entities are filtered to `distance <= radius`.

Response fields:

| Field | Type | Description |
| --- | --- | --- |
| `radius` | number | Effective radius used by the tool. |
| `count` | integer | Number of returned entities. |
| `entities` | array | Nearby entity entries. |

Each `entities[]` entry:

| Field | Type | Description |
| --- | --- | --- |
| `uuid` | string | Entity UUID. |
| `type` | string | Entity type registry id. |
| `name` | string | Display name. |
| `x` | number | Entity X coordinate. |
| `y` | number | Entity Y coordinate. |
| `z` | number | Entity Z coordinate. |
| `distance` | number | Distance from player in blocks. |

## `get_biome`

Arguments: none.

Response fields:

| Field | Type | Description |
| --- | --- | --- |
| `biome` | string | Current biome registry id. |

## `get_time_weather`

Arguments: none.

Response fields:

| Field | Type | Description |
| --- | --- | --- |
| `world_time` | integer | Absolute world time ticks. |
| `day` | integer | Day index based on world time. |
| `time_of_day` | integer | Tick within current day, `0..23999`. |
| `is_daytime` | boolean | True during daytime tick range. |
| `is_raining` | boolean | True when world is raining. |
| `is_thundering` | boolean | True when world is thundering. |
