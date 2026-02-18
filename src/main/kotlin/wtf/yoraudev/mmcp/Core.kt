package wtf.yoraudev.mmcp

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer

class Core : ModInitializer, ClientModInitializer {
    override fun onInitialize() {
        McpServerRuntime.start()
    }

    override fun onInitializeClient() {
        McpServerRuntime.start()
    }
}
