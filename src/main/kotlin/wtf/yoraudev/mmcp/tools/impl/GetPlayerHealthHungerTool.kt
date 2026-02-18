package wtf.yoraudev.mmcp.tools.impl

import io.modelcontextprotocol.spec.McpSchema
import org.slf4j.Logger
import wtf.yoraudev.mmcp.tools.base.StructuredGameTool
import wtf.yoraudev.mmcp.tools.base.GameContextProvider
import wtf.yoraudev.mmcp.tools.base.ToolResponses
import wtf.yoraudev.mmcp.tools.base.ToolSchemas

class GetPlayerHealthHungerTool(
    logger: Logger,
    responses: ToolResponses
) : StructuredGameTool(logger, responses) {
    override val schema: McpSchema.Tool = ToolSchemas.tool(
        "get_player_health_hunger",
        "Returns current health and hunger values.",
        ToolSchemas.emptyObject()
    )

    override val toolName: String = "get_player_health_hunger"

    override fun produce(arguments: Map<String, Any>): Map<String, Any> {
        val context = GameContextProvider.current()
        val hunger = context.player.hungerManager
        return mapOf(
            "health" to context.player.health.toDouble(),
            "max_health" to context.player.maxHealth.toDouble(),
            "absorption" to context.player.absorptionAmount.toDouble(),
            "food_level" to hunger.foodLevel,
            "saturation" to hunger.saturationLevel.toDouble()
        )
    }
}
