package wtf.yoraudev.mmcp.tools.base

import com.fasterxml.jackson.databind.ObjectMapper
import io.modelcontextprotocol.server.McpServer
import wtf.yoraudev.mmcp.tools.impl.GetBiomeTool
import wtf.yoraudev.mmcp.tools.impl.GetInventoryTool
import wtf.yoraudev.mmcp.tools.impl.GetNearbyBlocksTool
import wtf.yoraudev.mmcp.tools.impl.GetNearbyEntitiesTool
import wtf.yoraudev.mmcp.tools.impl.GetPlayerHealthHungerTool
import wtf.yoraudev.mmcp.tools.impl.GetPlayerPositionTool
import wtf.yoraudev.mmcp.tools.impl.GetTimeWeatherTool
import wtf.yoraudev.mmcp.tools.impl.PingTool
import java.util.function.BiFunction
import org.slf4j.Logger

class McpToolRegistry(
    objectMapper: ObjectMapper,
    logger: Logger
) {
    private val responses = ToolResponses(objectMapper)
    private val tools: List<MmcpTool> = listOf(
        PingTool(responses),
        GetPlayerPositionTool(logger, responses),
        GetPlayerHealthHungerTool(logger, responses),
        GetInventoryTool(logger, responses),
        GetNearbyBlocksTool(logger, responses),
        GetNearbyEntitiesTool(logger, responses),
        GetBiomeTool(logger, responses),
        GetTimeWeatherTool(logger, responses)
    )

    fun register(specification: McpServer.SyncSpecification<*>) {
        tools.forEach { tool ->
            specification.toolCall(
                tool.schema,
                BiFunction { _, request -> tool.execute(request) }
            )
        }
    }
}
