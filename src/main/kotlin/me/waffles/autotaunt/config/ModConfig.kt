package me.waffles.autotaunt.config

import me.waffles.autotaunt.AutoTaunt
import net.minecraft.client.Minecraft
import org.polyfrost.oneconfig.api.config.v1.Config
import org.polyfrost.oneconfig.api.config.v1.Tree
import org.polyfrost.oneconfig.api.config.v1.annotations.Dropdown
import org.polyfrost.oneconfig.api.config.v1.annotations.Include
import org.polyfrost.oneconfig.api.config.v1.annotations.Keybind
import org.polyfrost.oneconfig.api.config.v1.annotations.TextList
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import org.polyfrost.oneconfig.api.platform.v1.Platform
import org.polyfrost.oneconfig.api.ui.v1.keybind.KeybindHelper

object ModConfig : Config("autotaunt.json", AutoTaunt.NAME, Category.QOL) {
    @Keybind(title = "AutoTaunt keybind")
    var keyBind = KeybindHelper.builder().key(Platform.compatibility().keys().keyL)
        .action(Runnable { sendTaunt() }).build()

    @Dropdown(title = "Chat type", options = ["Default", "All chat", "Party chat", "Shout", "Smart"])
    var value = 0

    // Retained only to migrate settings from the first OneConfig v1 port.
    @Include
    var entries: Array<String> = emptyArray()

    @TextList(
        title = "Enabled taunts",
        description = "Messages used by the send key. Use /taunts toggle <number> to disable a message.",
        subcategory = "Taunts", placeholder = "Enter a taunt", addText = "Add taunt"
    )
    var taunts: Array<String> = emptyArray()

    @TextList(
        title = "Disabled taunts",
        description = "Saved messages that are not sent. Use /taunts toggle <number> to enable a message.",
        subcategory = "Taunts", placeholder = "Enter a taunt", addText = "Add disabled taunt"
    )
    var disabledTaunts: Array<String> = emptyArray()

    val macros: List<Macro>
        get() = taunts.map { Macro(it) } + disabledTaunts.map { Macro(it, false) }

    @Include
    var legacyMigrationChecked = false

    private val selector = TauntSelector()

    override fun onInitialized(tree: Tree) {
        super.onInitialized(tree)
        var migrated = false
        if (!legacyMigrationChecked) {
            LegacyConfigMigration.import(this)
            legacyMigrationChecked = true
            migrated = true
        }
        if (entries.isNotEmpty()) {
            val imported = MacroCodec.decode(entries)
            taunts = imported.filter { it.enabled }.map { it.text }.toTypedArray()
            disabledTaunts = imported.filterNot { it.enabled }.map { it.text }.toTypedArray()
            entries = emptyArray()
            migrated = true
        }
        if (migrated) save()
    }

    fun updateEntries(macros: List<Macro>) {
        getProperty("taunts").setAs(macros.filter { it.enabled }.map { it.text }.toTypedArray())
        getProperty("disabledTaunts").setAs(macros.filterNot { it.enabled }.map { it.text }.toTypedArray())
        save()
    }

    fun sendTaunt() {
        val player = Minecraft.getInstance().player ?: return
        val text = selector.next(macros) ?: return
        val hypixel = HypixelUtils.isHypixel()
        val mode = if (hypixel) HypixelUtils.getLocation().mode.orElse(null) else null
        player.sendChat(ChatRouter.route(text, value, hypixel, mode, player.abilities.canFly, AutoTaunt.ingame))
    }
}
