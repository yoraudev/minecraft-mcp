package wtf.yoraudev.mmcp.tools.impl

import io.modelcontextprotocol.spec.McpSchema
import wtf.yoraudev.mmcp.tools.base.MmcpTool
import wtf.yoraudev.mmcp.tools.base.ToolResponses
import wtf.yoraudev.mmcp.tools.base.ToolSchemas

class PingTool(
    private val responses: ToolResponses
) : MmcpTool {
    override val schema: McpSchema.Tool = ToolSchemas.tool(
        "mmcp.ping",
        "Returns pong from Minecraft MCP mod.",
        ToolSchemas.emptyObject()
    )

    override fun execute(request: McpSchema.CallToolRequest): McpSchema.CallToolResult {
        return responses.text("pong")
    }
}
