package wtf.yoraudev.mmcp.tools.base

import io.modelcontextprotocol.spec.McpSchema

object ToolSchemas {
    fun tool(name: String, description: String, inputSchema: McpSchema.JsonSchema): McpSchema.Tool {
        return McpSchema.Tool.builder()
            .name(name)
            .description(description)
            .inputSchema(inputSchema)
            .build()
    }

    fun emptyObject(): McpSchema.JsonSchema {
        return objectSchema(emptyMap())
    }

    fun objectSchema(
        properties: Map<String, Any>,
        required: List<String> = emptyList()
    ): McpSchema.JsonSchema {
        return McpSchema.JsonSchema("object", properties, required, false, emptyMap(), emptyMap())
    }
}
