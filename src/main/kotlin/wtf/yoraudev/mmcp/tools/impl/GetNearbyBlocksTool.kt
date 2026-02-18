package wtf.yoraudev.mmcp.tools.impl

import io.modelcontextprotocol.spec.McpSchema
import net.minecraft.registry.Registries
import net.minecraft.util.math.BlockPos
import org.slf4j.Logger
import wtf.yoraudev.mmcp.tools.base.ToolArgs
import wtf.yoraudev.mmcp.tools.base.StructuredGameTool
import wtf.yoraudev.mmcp.tools.base.GameContextProvider
import wtf.yoraudev.mmcp.tools.base.ToolResponses
import wtf.yoraudev.mmcp.tools.base.ToolSchemas

class GetNearbyBlocksTool(
    logger: Logger,
    responses: ToolResponses
) : StructuredGameTool(logger, responses) {
    override val schema: McpSchema.Tool = ToolSchemas.tool(
        "get_nearby_blocks",
        "Returns nearby blocks around the player.",
        ToolSchemas.objectSchema(
            mapOf(
                "radius" to mapOf("type" to "integer", "minimum" to 1, "maximum" to 12, "default" to 3),
                "include_air" to mapOf("type" to "boolean", "default" to false),
                "limit" to mapOf("type" to "integer", "minimum" to 1, "maximum" to 4096, "default" to 256)
            )
        )
    )

    override val toolName: String = "get_nearby_blocks"

    override fun produce(arguments: Map<String, Any>): Map<String, Any> {
        val context = GameContextProvider.current()
        val radius = ToolArgs.int(arguments, "radius", 3, 1, 12)
        val includeAir = ToolArgs.boolean(arguments, "include_air", false)
        val limit = ToolArgs.int(arguments, "limit", 256, 1, 4096)
        val center = context.player.blockPos
        val mutablePos = BlockPos.Mutable()
        val blocks = ArrayList<Map<String, Any>>(limit)

        search@ for (x in -radius..radius) {
            for (y in -radius..radius) {
                for (z in -radius..radius) {
                    if (blocks.size >= limit) {
                        break@search
                    }
                    mutablePos.set(center.x + x, center.y + y, center.z + z)
                    val blockState = context.world.getBlockState(mutablePos)
                    if (!includeAir && blockState.isAir) {
                        continue
                    }
                    blocks.add(
                        mapOf(
                            "x" to mutablePos.x,
                            "y" to mutablePos.y,
                            "z" to mutablePos.z,
                            "block_id" to Registries.BLOCK.getId(blockState.block).toString()
                        )
                    )
                }
            }
        }

        return mapOf(
            "radius" to radius,
            "include_air" to includeAir,
            "count" to blocks.size,
            "blocks" to blocks
        )
    }
}
