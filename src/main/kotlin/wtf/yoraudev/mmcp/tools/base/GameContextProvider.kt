package wtf.yoraudev.mmcp.tools.base

import net.minecraft.client.MinecraftClient

object GameContextProvider {
    fun current(): GameContext {
        val client = MinecraftClient.getInstance()
        val player = client.player ?: throw IllegalStateException("Player is not available. Join a world first.")
        val world = client.world ?: throw IllegalStateException("World is not available. Join a world first.")
        return GameContext(client, player, world)
    }
}
