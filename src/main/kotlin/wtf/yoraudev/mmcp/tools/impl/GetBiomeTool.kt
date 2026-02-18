package wtf.yoraudev.mmcp.tools.impl

import io.modelcontextprotocol.spec.McpSchema
import net.minecraft.registry.RegistryKeys
import org.slf4j.Logger
import wtf.yoraudev.mmcp.tools.base.StructuredGameTool
import wtf.yoraudev.mmcp.tools.base.GameContextProvider
import wtf.yoraudev.mmcp.tools.base.ToolResponses
import wtf.yoraudev.mmcp.tools.base.ToolSchemas

class GetBiomeTool(
    logger: Logger,
    responses: ToolResponses
) : StructuredGameTool(logger, responses) {
    override val schema: McpSchema.Tool = ToolSchemas.tool(
        "get_biome",
        "Returns the current biome at the player position.",
        ToolSchemas.emptyObject()
    )

    override val toolName: String = "get_biome"

    override fun produce(arguments: Map<String, Any>): Map<String, Any> {
        val context = GameContextProvider.current()
        val biomeEntry = context.world.getBiome(context.player.blockPos)
        val biomeId = context.world.registryManager
            .getOrThrow(RegistryKeys.BIOME)
            .getId(biomeEntry.value())
            ?.toString()
            ?: "unknown"
        return mapOf("biome" to biomeId)
    }
}
