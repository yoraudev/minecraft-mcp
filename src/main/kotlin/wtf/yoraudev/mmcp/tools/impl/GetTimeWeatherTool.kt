package wtf.yoraudev.mmcp.tools.impl

import io.modelcontextprotocol.spec.McpSchema
import org.slf4j.Logger
import wtf.yoraudev.mmcp.tools.base.StructuredGameTool
import wtf.yoraudev.mmcp.tools.base.GameContextProvider
import wtf.yoraudev.mmcp.tools.base.ToolResponses
import wtf.yoraudev.mmcp.tools.base.ToolSchemas

class GetTimeWeatherTool(
    logger: Logger,
    responses: ToolResponses
) : StructuredGameTool(logger, responses) {
    override val schema: McpSchema.Tool = ToolSchemas.tool(
        "get_time_weather",
        "Returns current world time and weather info.",
        ToolSchemas.emptyObject()
    )

    override val toolName: String = "get_time_weather"

    override fun produce(arguments: Map<String, Any>): Map<String, Any> {
        val context = GameContextProvider.current()
        val absoluteTime = context.world.timeOfDay
        val timeOfDay = Math.floorMod(absoluteTime, 24000L)
        return mapOf(
            "world_time" to absoluteTime,
            "day" to Math.floorDiv(absoluteTime, 24000L),
            "time_of_day" to timeOfDay,
            "is_daytime" to (timeOfDay in 0L..12300L),
            "is_raining" to context.world.isRaining,
            "is_thundering" to context.world.isThundering
        )
    }
}
