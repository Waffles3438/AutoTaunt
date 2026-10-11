package me.waffles.autotaunt.command

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import me.waffles.autotaunt.config.Macro
import me.waffles.autotaunt.config.ModConfig
import org.polyfrost.oneconfig.api.commands.v1.CommandManager
import org.polyfrost.oneconfig.api.platform.v1.Platform
import org.polyfrost.oneconfig.utils.v1.dsl.openUI

object TauntsCommand {
    private val defaultTaunts = listOf(
        "Your fucking dogshit", "Stop fucking running", "Ur so dogshit at the game", "Just log off",
        "Waste of space", "Imagine being this bad", "Olympic runners run less than you",
        "Holy fuck just stop playing the game", "Uninstall the game", "So fucking bad", "L"
    )

    fun register() {
        CommandManager.register(CommandManager.literal("taunts")
            .executes { ModConfig.openUI(); 1 }
            .then(CommandManager.literal("help").executes {
                chat("/taunts — open settings")
                chat("/taunts add <text> — add a taunt")
                chat("/taunts clear — remove all taunts")
                chat("/taunts defaultTaunts — add default taunts")
                chat("/taunts list, remove <number>, toggle <number>")
                1
            })
            .then(CommandManager.literal("add").then(
                CommandManager.argument("text", StringArgumentType.greedyString()).executes { context ->
                    val text = StringArgumentType.getString(context, "text")
                    if (text.isBlank()) { chat("Enter a non-empty taunt."); 0 }
                    else { ModConfig.updateEntries(ModConfig.macros + Macro(text)); chat("Added taunt."); 1 }
                }))
            .then(CommandManager.literal("clear").executes {
                ModConfig.updateEntries(emptyList()); chat("Cleared list of taunts!"); 1
            })
            .then(CommandManager.literal("defaultTaunts").executes {
                ModConfig.updateEntries(ModConfig.macros + defaultTaunts.map { Macro(it) })
                chat("Added default taunts"); 1
            })
            .then(CommandManager.literal("list").executes {
                if (ModConfig.macros.isEmpty()) chat("No taunts yet. Use /taunts add <text>.")
                ModConfig.macros.forEachIndexed { i, macro ->
                    chat("${i + 1}. [${if (macro.enabled) "on" else "off"}] ${macro.text}")
                }
                1
            })
            .then(CommandManager.literal("remove").then(
                CommandManager.argument("number", IntegerArgumentType.integer(1)).executes { context ->
                    edit(IntegerArgumentType.getInteger(context, "number"), remove = true)
                }))
            .then(CommandManager.literal("toggle").then(
                CommandManager.argument("number", IntegerArgumentType.integer(1)).executes { context ->
                    edit(IntegerArgumentType.getInteger(context, "number"), remove = false)
                }))
        )
    }

    private fun edit(number: Int, remove: Boolean): Int {
        val entries = ModConfig.macros.toMutableList()
        if (number !in 1..entries.size) { chat("No taunt with that number. Use /taunts list."); return 0 }
        if (remove) entries.removeAt(number - 1)
        else entries[number - 1] = entries[number - 1].let { it.copy(enabled = !it.enabled) }
        ModConfig.updateEntries(entries)
        chat(if (remove) "Removed taunt." else "Toggled taunt.")
        return 1
    }

    private fun chat(text: String) = Platform.compatibility().displayChatMessage(text)
}
