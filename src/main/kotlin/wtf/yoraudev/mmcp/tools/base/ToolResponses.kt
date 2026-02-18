package wtf.yoraudev.mmcp.tools.base

import com.fasterxml.jackson.databind.ObjectMapper
import io.modelcontextprotocol.spec.McpSchema

class ToolResponses(
    private val objectMapper: ObjectMapper
) {
    fun text(text: String, isError: Boolean = false): McpSchema.CallToolResult {
        return McpSchema.CallToolResult.builder()
            .addTextContent(text)
            .isError(isError)
            .build()
    }

    fun structured(payload: Map<String, Any>, isError: Boolean = false): McpSchema.CallToolResult {
        return McpSchema.CallToolResult.builder()
            .addTextContent(payloadText(payload))
            .structuredContent(payload)
            .isError(isError)
            .build()
    }

    private fun payloadText(payload: Map<String, Any>): String {
        return runCatching { objectMapper.writeValueAsString(payload) }
            .getOrElse { payload.toString() }
    }
}
