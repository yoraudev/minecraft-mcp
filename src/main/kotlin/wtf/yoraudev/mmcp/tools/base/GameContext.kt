package wtf.yoraudev.mmcp.tools.base

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.world.ClientWorld

data class GameContext(
    val client: MinecraftClient,
    val player: ClientPlayerEntity,
    val world: ClientWorld
)
