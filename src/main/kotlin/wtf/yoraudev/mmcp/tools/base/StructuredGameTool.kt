package wtf.yoraudev.mmcp.tools.base

import io.modelcontextprotocol.spec.McpSchema
import org.slf4j.Logger

abstract class StructuredGameTool(
    private val logger: Logger,
    private val responses: ToolResponses
) : MmcpTool {
    protected abstract val toolName: String
    protected abstract fun produce(arguments: Map<String, Any>): Map<String, Any>

    final override fun execute(request: McpSchema.CallToolRequest): McpSchema.CallToolResult {
        return runCatching {
            produce(request.arguments() ?: emptyMap())
        }.fold(
            onSuccess = { responses.structured(it) },
            onFailure = {
                logger.warn("Tool {} failed", toolName, it)
                responses.structured(
                    mapOf(
                        "tool" to toolName,
                        "error" to (it.message ?: "Unknown error")
                    ),
                    true
                )
            }
        )
    }
}
