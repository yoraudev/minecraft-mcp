package wtf.yoraudev.mmcp.tools.impl

import io.modelcontextprotocol.spec.McpSchema
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.registry.Registries
import org.slf4j.Logger
import wtf.yoraudev.mmcp.tools.base.ToolArgs
import wtf.yoraudev.mmcp.tools.base.StructuredGameTool
import wtf.yoraudev.mmcp.tools.base.GameContextProvider
import wtf.yoraudev.mmcp.tools.base.ToolResponses
import wtf.yoraudev.mmcp.tools.base.ToolSchemas

class GetNearbyEntitiesTool(
    logger: Logger,
    responses: ToolResponses
) : StructuredGameTool(logger, responses) {
    override val schema: McpSchema.Tool = ToolSchemas.tool(
        "get_nearby_entities",
        "Returns nearby entities around the player.",
        ToolSchemas.objectSchema(
            mapOf(
                "radius" to mapOf("type" to "number", "minimum" to 1.0, "maximum" to 128.0, "default" to 32.0),
                "limit" to mapOf("type" to "integer", "minimum" to 1, "maximum" to 256, "default" to 64),
                "include_players" to mapOf("type" to "boolean", "default" to true)
            )
        )
    )

    override val toolName: String = "get_nearby_entities"

    override fun produce(arguments: Map<String, Any>): Map<String, Any> {
        val context = GameContextProvider.current()
        val radius = ToolArgs.double(arguments, "radius", 32.0, 1.0, 128.0)
        val maxDistanceSquared = radius * radius
        val limit = ToolArgs.int(arguments, "limit", 64, 1, 256)
        val includePlayers = ToolArgs.boolean(arguments, "include_players", true)
        val entities = context.world.getOtherEntities(context.player, context.player.boundingBox.expand(radius))
            .asSequence()
            .filter { includePlayers || it !is PlayerEntity }
            .filter { context.player.squaredDistanceTo(it) <= maxDistanceSquared }
            .sortedBy { context.player.squaredDistanceTo(it) }
            .take(limit)
            .map {
                mapOf(
                    "uuid" to it.uuidAsString,
                    "type" to Registries.ENTITY_TYPE.getId(it.type).toString(),
                    "name" to it.name.string,
                    "x" to it.x,
                    "y" to it.y,
                    "z" to it.z,
                    "distance" to context.player.distanceTo(it).toDouble()
                )
            }
            .toList()

        return mapOf(
            "radius" to radius,
            "count" to entities.size,
            "entities" to entities
        )
    }
}
