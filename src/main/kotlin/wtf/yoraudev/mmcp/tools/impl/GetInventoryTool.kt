package wtf.yoraudev.mmcp.tools.impl

import io.modelcontextprotocol.spec.McpSchema
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import org.slf4j.Logger
import wtf.yoraudev.mmcp.tools.base.StructuredGameTool
import wtf.yoraudev.mmcp.tools.base.GameContextProvider
import wtf.yoraudev.mmcp.tools.base.ToolResponses
import wtf.yoraudev.mmcp.tools.base.ToolSchemas

class GetInventoryTool(
    logger: Logger,
    responses: ToolResponses
) : StructuredGameTool(logger, responses) {
    override val schema: McpSchema.Tool = ToolSchemas.tool(
        "get_inventory",
        "Returns non-empty inventory slots for the player.",
        ToolSchemas.emptyObject()
    )

    override val toolName: String = "get_inventory"

    override fun produce(arguments: Map<String, Any>): Map<String, Any> {
        val context = GameContextProvider.current()
        val inventory = context.player.inventory
        val items = mutableListOf<Map<String, Any>>()

        for (slot in 0 until inventory.size()) {
            addStack(items, slot, sectionFor(slot), inventory.getStack(slot))
        }

        return mapOf(
            "selected_slot" to inventory.selectedSlot,
            "item_count" to items.size,
            "items" to items
        )
    }

    private fun sectionFor(slot: Int): String {
        return when {
            slot in 0..8 -> "hotbar"
            slot in 9..35 -> "main"
            slot in 36..39 -> "armor"
            slot == 40 -> "offhand"
            else -> "extra"
        }
    }

    private fun addStack(
        items: MutableList<Map<String, Any>>,
        slot: Int,
        section: String,
        stack: ItemStack
    ) {
        if (stack.isEmpty) {
            return
        }
        items.add(
            mapOf(
                "section" to section,
                "slot" to slot,
                "item_id" to Registries.ITEM.getId(stack.item).toString(),
                "count" to stack.count
            )
        )
    }
}
