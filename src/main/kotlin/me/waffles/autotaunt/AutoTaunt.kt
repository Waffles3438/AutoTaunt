package me.waffles.autotaunt

import cc.polyfrost.oneconfig.renderer.asset.SVG
import cc.polyfrost.oneconfig.utils.commands.CommandManager
import me.waffles.autotaunt.command.TauntsCommand
import me.waffles.autotaunt.config.ModConfig
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(modid = AutoTaunt.MODID, name = AutoTaunt.NAME, version = AutoTaunt.VERSION, modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter")
object AutoTaunt {
    const val MODID: String = "@ID@"
    const val NAME: String = "@NAME@"
    const val VERSION: String = "@VER@"

    val PLUS_ICON = SVG("/assets/autotaunt/plus.svg")
    val MINUS_ICON = SVG("/assets/autotaunt/minus.svg")

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent?) {
        CommandManager.INSTANCE.registerCommand(TauntsCommand())
        ModConfig
    }
}