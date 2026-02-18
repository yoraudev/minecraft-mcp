package wtf.yoraudev.mmcp.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientStreamableHttpTransport;
import io.modelcontextprotocol.spec.McpSchema;

public final class MmcpClientMain {
    private static final String MOD_BASE_URL = "http://127.0.0.1:47355";
    private static final String MOD_ENDPOINT = "/mcp";

    private MmcpClientMain() {
    }

    public static void main(String[] args) {
        int exitCode = run(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    private static int run(String[] args) {
        if (args.length != 0) {
            System.out.println("Usage: java -jar mmcp.jar");
            return 1;
        }

        HttpClientStreamableHttpTransport transport = HttpClientStreamableHttpTransport.builder(MOD_BASE_URL)
            .endpoint(MOD_ENDPOINT)
            .jsonMapper(new io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper(new ObjectMapper()))
            .build();

        try (McpSyncClient client = McpClient.sync(transport)
            .clientInfo(new McpSchema.Implementation("mmcp-client", "0.1.0"))
            .capabilities(McpSchema.ClientCapabilities.builder().roots(false).build())
            .build()) {
            McpSchema.InitializeResult initializeResult = client.initialize();
            McpSchema.Implementation serverInfo = initializeResult.serverInfo();
            System.out.println("Connected to " + serverInfo.name() + " " + serverInfo.version());

            McpSchema.ListToolsResult listToolsResult = client.listTools();
            if (listToolsResult.tools() == null || listToolsResult.tools().isEmpty()) {
                System.out.println("No tools returned by the server.");
                return 0;
            }

            System.out.println("Tools:");
            for (McpSchema.Tool tool : listToolsResult.tools()) {
                String description = tool.description() == null ? "" : tool.description();
                System.out.println("- " + tool.name() + " " + description);
            }

            return 0;
        } catch (Exception exception) {
            System.err.println("Could not connect to Minecraft MCP mod server at " + MOD_BASE_URL + MOD_ENDPOINT);
            System.err.println("Start Minecraft with the MMCP mod loaded and try again.");
            System.err.println("Details: " + exception.getMessage());
            exception.printStackTrace(System.err);
            return 1;
        }
    }
}
