package wtf.yoraudev.mmcp

import com.fasterxml.jackson.databind.ObjectMapper
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper
import io.modelcontextprotocol.server.McpServer
import io.modelcontextprotocol.server.McpSyncServer
import io.modelcontextprotocol.server.transport.HttpServletStreamableServerTransportProvider
import io.modelcontextprotocol.spec.McpSchema
import java.net.InetSocketAddress
import java.util.concurrent.atomic.AtomicBoolean
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.slf4j.LoggerFactory
import wtf.yoraudev.mmcp.tools.base.McpToolRegistry

object McpServerRuntime {
    private const val HOST = "127.0.0.1"
    private const val PORT = 47355
    private const val ENDPOINT = "/mcp"
    private val logger = LoggerFactory.getLogger("minecraft-mcp")
    private val started = AtomicBoolean(false)
    private val objectMapper = ObjectMapper()
    private var server: McpSyncServer? = null
    private var httpServer: Server? = null

    fun start() {
        if (!started.compareAndSet(false, true)) {
            return
        }

        runCatching {
            val mapper = JacksonMcpJsonMapper(objectMapper)
            val transport = HttpServletStreamableServerTransportProvider.builder()
                .jsonMapper(mapper)
                .mcpEndpoint(ENDPOINT)
                .build()

            val specification = McpServer.sync(transport)
                .serverInfo("minecraft-mcp", "0.1.0")
                .capabilities(McpSchema.ServerCapabilities.builder().tools(false).build())

            McpToolRegistry(objectMapper, logger).register(specification)
            server = specification.build()

            val jetty = Server(InetSocketAddress(HOST, PORT))
            val context = ServletContextHandler(ServletContextHandler.NO_SESSIONS)
            context.contextPath = "/"
            context.addServlet(ServletHolder(transport), "/*")
            jetty.handler = context
            jetty.start()
            httpServer = jetty

            Runtime.getRuntime().addShutdownHook(
                Thread {
                    runCatching { server?.closeGracefully() }
                    runCatching { httpServer?.stop() }
                }
            )

            logger.info("MMCP MCP server is running at http://{}:{}{}", HOST, PORT, ENDPOINT)
        }.onFailure {
            started.set(false)
            logger.error("MMCP MCP server failed to start", it)
        }
    }
}
