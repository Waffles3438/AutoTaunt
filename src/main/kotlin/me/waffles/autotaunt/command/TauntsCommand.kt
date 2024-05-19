package me.waffles.autotaunt.command

import cc.polyfrost.oneconfig.libs.universal.UChat
import cc.polyfrost.oneconfig.utils.commands.annotations.Command
import cc.polyfrost.oneconfig.utils.commands.annotations.Greedy
import cc.polyfrost.oneconfig.utils.commands.annotations.Main
import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand
import me.waffles.autotaunt.config.Macro
import me.waffles.autotaunt.config.ModConfig
import me.waffles.autotaunt.element.MacroListOption
import me.waffles.autotaunt.element.WrappedMacro

@Command(value = "taunts", description = "Auto Taunt")
class TauntsCommand {

    private val defaultTaunts = listOf(
        "Your fucking dogshit",
        "Stop fucking running",
        "Ur so dogshit at the game",
        "Just log off",
        "Waste of space",
        "Imagine being this bad",
        "Olympic runners run less than you",
        "Holy fuck just stop playing the game",
        "Uninstall the game",
        "So fucking bad",
        "L"
    )

    @Main
    private fun main() {
        UChat.chat("clear - removes all taunts")
        UChat.chat("defaultTaunts - adds default taunts")
    }

    @SubCommand
    private fun add(@Greedy text: String) {
        MacroListOption.wrappedMacros.add(WrappedMacro(Macro(text)))
        ModConfig.save()
    }

    @SubCommand
    private fun clear() {
        MacroListOption.wrappedMacros.clear()
        ModConfig.save()
        UChat.chat("Cleared list of taunts!")
    }

    @SubCommand
    private fun defaultTaunts() {
        for (text in defaultTaunts) {
            MacroListOption.wrappedMacros.add(WrappedMacro(Macro(text)))
        }
        ModConfig.save()
        UChat.chat("Added default taunts")
    }
}