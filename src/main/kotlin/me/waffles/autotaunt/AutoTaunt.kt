package me.waffles.autotaunt

import cc.polyfrost.oneconfig.events.EventManager
import cc.polyfrost.oneconfig.events.event.WorldLoadEvent
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import cc.polyfrost.oneconfig.renderer.asset.SVG
import cc.polyfrost.oneconfig.utils.commands.CommandManager
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils
import me.waffles.autotaunt.command.TauntsCommand
import me.waffles.autotaunt.config.ModConfig
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod(modid = AutoTaunt.MODID, name = AutoTaunt.NAME, version = AutoTaunt.VERSION, modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter")
object AutoTaunt {
    const val MODID: String = "@ID@"
    const val NAME: String = "@NAME@"
    const val VERSION: String = "@VER@"

    val PLUS_ICON = SVG("/assets/autotaunt/plus.svg")
    val MINUS_ICON = SVG("/assets/autotaunt/minus.svg")

    var ingame = false

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent?) {
        CommandManager.INSTANCE.registerCommand(TauntsCommand())
        ModConfig
        PatternHandler.initialize()
        EventManager.INSTANCE.register(this)
        MinecraftForge.EVENT_BUS.register(this)
    }

    @Subscribe
    fun onWorldLoad(e: WorldLoadEvent) {
        ingame = false
    }

    @SubscribeEvent
    fun onReceive(e: ClientChatReceivedEvent) {
        if (!HypixelUtils.INSTANCE.isHypixel) return
        val message = EnumChatFormatting.getTextWithoutFormattingCodes(e.message.unformattedText)
        if (hasGameEnded(message)) {
            ingame = false
        }
        if (message.contains(": ")) return
        if (message.contains("The game starts in 1 second!") || (message.contains("You have respawned!") && ingame)) {
            ingame = true
        }
    }

    private fun hasGameEnded(message: String): Boolean {
        if (PatternHandler.gameEnd.isNotEmpty()) {
            for (triggers in PatternHandler.gameEnd) {
                if (triggers.matcher(message).matches()) {
                    return true
                }
            }
        }

        return false
    }

}
