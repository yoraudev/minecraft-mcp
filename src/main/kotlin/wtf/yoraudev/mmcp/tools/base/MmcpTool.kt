package wtf.yoraudev.mmcp.tools.base

import io.modelcontextprotocol.spec.McpSchema

interface MmcpTool {
    val schema: McpSchema.Tool
    fun execute(request: McpSchema.CallToolRequest): McpSchema.CallToolResult
}
