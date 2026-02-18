package wtf.yoraudev.mmcp.tools.impl

import io.modelcontextprotocol.spec.McpSchema
import org.slf4j.Logger
import wtf.yoraudev.mmcp.tools.base.StructuredGameTool
import wtf.yoraudev.mmcp.tools.base.GameContextProvider
import wtf.yoraudev.mmcp.tools.base.ToolResponses
import wtf.yoraudev.mmcp.tools.base.ToolSchemas

class GetPlayerPositionTool(
    logger: Logger,
    responses: ToolResponses
) : StructuredGameTool(logger, responses) {
    override val schema: McpSchema.Tool = ToolSchemas.tool(
        "get_player_position",
        "Returns the current player position and rotation.",
        ToolSchemas.emptyObject()
    )

    override val toolName: String = "get_player_position"

    override fun produce(arguments: Map<String, Any>): Map<String, Any> {
        val context = GameContextProvider.current()
        return mapOf(
            "x" to context.player.x,
            "y" to context.player.y,
            "z" to context.player.z,
            "yaw" to context.player.yaw.toDouble(),
            "pitch" to context.player.pitch.toDouble(),
            "block_x" to context.player.blockX,
            "block_y" to context.player.blockY,
            "block_z" to context.player.blockZ,
            "dimension" to context.world.registryKey.value.toString(),
            "server_address" to (context.client.currentServerEntry?.address ?: "singleplayer")
        )
    }
}
