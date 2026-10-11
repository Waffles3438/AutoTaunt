package me.waffles.autotaunt

import me.waffles.autotaunt.command.TauntsCommand
import me.waffles.autotaunt.config.ModConfig
import net.fabricmc.api.ClientModInitializer
import org.polyfrost.oneconfig.api.event.v1.EventManager
import org.polyfrost.oneconfig.api.event.v1.events.ChatEvent
import org.polyfrost.oneconfig.api.event.v1.events.WorldEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils

object AutoTaunt : ClientModInitializer {
    const val MODID = "autotaunt"
    const val NAME = "AutoTaunt"
    var ingame = false
        private set

    override fun onInitializeClient() {
        ModConfig.preload()
        TauntsCommand.register()
        PatternHandler.initialize()
        EventManager.INSTANCE.register(this)
    }

    @Subscribe
    fun onWorldLoad(event: WorldEvent.Load) { ingame = false }

    @Subscribe
    fun onWorldUnload(event: WorldEvent.Unload) { ingame = false }

    @Subscribe
    fun onReceive(event: ChatEvent.Receive) {
        if (!HypixelUtils.isHypixel()) return
        val message = event.fullyUnformattedMessage
        if (PatternHandler.gameEnd.any { it.matcher(message).matches() }) ingame = false
        if (message.contains(": ")) return
        if (message.contains("The game starts in 1 second!") ||
            (message.contains("You have respawned!") && ingame)) ingame = true
    }
}
