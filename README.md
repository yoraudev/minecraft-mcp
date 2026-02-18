<p align="center">
  <img src="src/main/resources/assets/minecraft-mcp/icon.png" alt="Minecraft MCP Logo" width="140">
</p>

<h1 align="center">Minecraft MCP</h1>

<p align="center">Minecraft MCP is a Fabric mod that exposes Minecraft world capabilities to AI assistants through MCP tools and resources.</p>

<p align="center">
  <a href="https://github.com/yoraudev/minecraft-mcp"><img src="https://img.shields.io/github/last-commit/yoraudev/minecraft-mcp?style=for-the-badge" alt="Last Commit"></a>
  <a href="https://github.com/yoraudev/minecraft-mcp"><img src="https://img.shields.io/github/languages/code-size/yoraudev/minecraft-mcp?style=for-the-badge" alt="Code Size"></a>
</p>

<h1 align="center">Requirements</h1>

- Java 21
- Gradle 9+
- Minecraft 1.21.11
- Fabric Loader 0.18.4+

<h1 align="center">Quick Start</h1>

1. Clone the repository.
2. Open the `minecraft-mcp` directory.
3. Build all artifacts:

```bash
gradle build
```

4. Use `minecraft-mcp-<version>.jar` as the Fabric mod file.
- Put this file into your Minecraft `mods` folder, or start from IDE with `gradle runClient`.
- This file runs the in-game MCP server at `http://127.0.0.1:47355/mcp`.

5. Use `mmcp.jar` as the MCP stdio proxy file.
- Start it from your MCP client configuration:

```bash
java -jar mmcp.jar
```

6. Add the proxy to your MCP Client:
- See the `Client Setup` section below.

7. Keep Minecraft running with the mod loaded while using MCP tools.

<h1 align="center">Client Setup</h1>

Codex CLI:

```bash
codex mcp add minecraft-mcp -- java -jar path/to/minecraft-mcp/build/libs/mmcp.jar
```

<h1 align="center">MCP Tools</h1>

| Tool | Description |
| --- | --- |
| `mmcp.ping` | Returns pong from Minecraft MCP mod. |
| `get_player_position` | Returns the current player position and rotation. |
| `get_player_health_hunger` | Returns current health and hunger values. |
| `get_inventory` | Returns non-empty inventory slots for the player. |
| `get_nearby_blocks` | Returns nearby blocks around the player. |
| `get_nearby_entities` | Returns nearby entities around the player. |
| `get_biome` | Returns the current biome at the player position. |
| `get_time_weather` | Returns current world time and weather info. |

Detailed specifications are in `TOOLS.md`.

<h2 align="center">Project Guidelines</h2>

Please read `CONTRIBUTING.md` before opening a pull request.

Please read `SECURITY.md` to report vulnerabilities responsibly.

This project follows `CODE_OF_CONDUCT.md`.

This project is licensed under the terms in `LICENSE`.
