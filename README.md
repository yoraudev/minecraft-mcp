<p align="center">
  <img src="src/main/resources/assets/minecraft-mcp/icon.png" alt="Minecraft MCP Logo" width="140">
</p>

<h1 align="center">Minecraft MCP</h1>

---

<p align="center">Minecraft MCP is a Fabric mod that exposes Minecraft world capabilities to AI assistants through MCP tools and resources.</p>

<p align="center">
  <a href="https://github.com/yoraudev/minecraft-mcp"><img src="https://img.shields.io/github/last-commit/yoraudev/minecraft-mcp?style=for-the-badge" alt="Last Commit"></a>
  <a href="https://github.com/yoraudev/minecraft-mcp"><img src="https://img.shields.io/github/languages/code-size/yoraudev/minecraft-mcp?style=for-the-badge" alt="Code Size"></a>
  <a href="https://github.com/yoraudev/minecraft-mcp"><img src="https://img.shields.io/tokei/lines/github/yoraudev/minecraft-mcp?style=for-the-badge" alt="Lines of Code"></a>
</p>

<h1 align="center">Requirements</h1>

---

- Java 21
- Gradle 9+
- Minecraft 1.21.11
- Fabric Loader 0.18.4+

<h1 align="center">Quick Start and Run Modes</h1>

---

1. Clone the repository.
2. Open the `minecraft-mcp` directory.
3. Build the project:

```bash
gradle build
```

This repository supports two run modes from one project.

1. Fabric Mod mode:
- Put the remapped mod jar into `mods` or use `gradle runClient`.
- MCP server starts automatically and listens at `http://127.0.0.1:47355/mcp`.

2. MCP Client mode:
- Build the client jar with `gradle mmcpCliJar`.
- Run:

```bash
java -jar build/libs/mmcp.jar
```

<h2 align="center">Project Guidelines</h2>

---

Please read `CONTRIBUTING.md` before opening a pull request.

Please read `SECURITY.md` to report vulnerabilities responsibly.

This project follows `CODE_OF_CONDUCT.md`.

This project is licensed under the terms in `LICENSE`.
