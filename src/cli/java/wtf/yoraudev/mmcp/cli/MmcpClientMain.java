package wtf.yoraudev.mmcp.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientStreamableHttpTransport;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import java.util.List;
import java.util.Map;

public final class MmcpClientMain {
    private static final String MOD_BASE_URL = "http://127.0.0.1:47355";
    private static final String MOD_ENDPOINT = "/mcp";
    private static final String PROXY_NAME = "minecraft-mcp-proxy";
    private static final String PROXY_VERSION = "0.1.0";

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
            System.err.println("Usage: java -jar mmcp.jar");
            return 1;
        }

        JacksonMcpJsonMapper mapper = new JacksonMcpJsonMapper(new ObjectMapper());
        UpstreamProxyClient upstream = new UpstreamProxyClient(mapper);
        List<McpSchema.Tool> tools;
        try {
            tools = upstream.listTools();
        } catch (Exception exception) {
            System.err.println("Could not connect to Minecraft MCP mod server at " + MOD_BASE_URL + MOD_ENDPOINT);
            System.err.println("Start Minecraft with the MMCP mod loaded and try again.");
            System.err.println("Details: " + exception.getMessage());
            return 1;
        }

        StdioServerTransportProvider transport = new StdioServerTransportProvider(mapper, System.in, System.out);
        McpServer.SingleSessionSyncSpecification specification = McpServer.sync(transport);
        specification.serverInfo(PROXY_NAME, PROXY_VERSION);
        specification.capabilities(McpSchema.ServerCapabilities.builder().tools(false).build());

        for (McpSchema.Tool tool : tools) {
            specification.toolCall(tool, (exchange, request) -> upstream.callTool(request));
        }

        try {
            McpSyncServer server = specification.build();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                upstream.close();
                try {
                    server.closeGracefully();
                } catch (Exception ignored) {
                    server.close();
                }
            }));
            return 0;
        } catch (Exception exception) {
            System.err.println("Could not connect to Minecraft MCP mod server at " + MOD_BASE_URL + MOD_ENDPOINT);
            System.err.println("Start Minecraft with the MMCP mod loaded and try again.");
            System.err.println("Details: " + exception.getMessage());
            upstream.close();
            return 1;
        }
    }

    private static final class UpstreamProxyClient implements AutoCloseable {
        private final Object lock = new Object();
        private final JacksonMcpJsonMapper mapper;
        private McpSyncClient client;

        private UpstreamProxyClient(JacksonMcpJsonMapper mapper) {
            this.mapper = mapper;
        }

        private List<McpSchema.Tool> listTools() {
            synchronized (lock) {
                McpSchema.ListToolsResult result = ensureClient().listTools();
                if (result.tools() == null) {
                    return List.of();
                }
                return result.tools();
            }
        }

        private McpSchema.CallToolResult callTool(McpSchema.CallToolRequest request) {
            synchronized (lock) {
                try {
                    return ensureClient().callTool(request);
                } catch (Exception firstFailure) {
                    resetClient();
                    try {
                        return ensureClient().callTool(request);
                    } catch (Exception secondFailure) {
                        return McpSchema.CallToolResult.builder()
                            .isError(true)
                            .textContent(List.of(
                                "Failed to call upstream tool " + request.name() + ": " + secondFailure.getMessage()
                            ))
                            .structuredContent(
                                Map.of(
                                    "tool", request.name(),
                                    "error", secondFailure.getMessage() == null
                                        ? "Unknown upstream error"
                                        : secondFailure.getMessage()
                                )
                            )
                            .build();
                    }
                }
            }
        }

        private McpSyncClient ensureClient() {
            if (client != null) {
                return client;
            }
            HttpClientStreamableHttpTransport transport = HttpClientStreamableHttpTransport.builder(MOD_BASE_URL)
                .endpoint(MOD_ENDPOINT)
                .jsonMapper(mapper)
                .build();
            client = McpClient.sync(transport)
                .clientInfo(new McpSchema.Implementation("mmcp-client", PROXY_VERSION))
                .capabilities(McpSchema.ClientCapabilities.builder().roots(false).build())
                .build();
            client.initialize();
            return client;
        }

        private void resetClient() {
            if (client == null) {
                return;
            }
            try {
                client.closeGracefully();
            } catch (Exception ignored) {
                client.close();
            } finally {
                client = null;
            }
        }

        @Override
        public void close() {
            synchronized (lock) {
                resetClient();
            }
        }
    }
}
