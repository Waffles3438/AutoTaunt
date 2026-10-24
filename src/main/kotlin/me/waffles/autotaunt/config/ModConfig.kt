package me.waffles.autotaunt.config

import me.waffles.autotaunt.AutoTaunt
import me.waffles.autotaunt.element.TauntList
import net.kyori.adventure.text.Component
import net.minecraft.client.Minecraft
import org.polyfrost.oneconfig.api.config.v1.Config
import org.polyfrost.oneconfig.api.config.v1.Tree
import org.polyfrost.oneconfig.api.config.v1.annotations.Dropdown
import org.polyfrost.oneconfig.api.config.v1.annotations.Include
import org.polyfrost.oneconfig.api.config.v1.annotations.Keybind
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

    @TauntList(subcategory = "Taunts")
    var taunts: Array<String> = emptyArray()

    // Taunts switched off with /taunts toggle. Stored but not shown in the settings page.
    @Include
    var disabledTaunts: Array<String> = emptyArray()

    val macros: List<Macro>
        get() = taunts.map { Macro(it) } + disabledTaunts.map { Macro(it, false) }

    @Include
    var legacyMigrationChecked = false

    private val selector = TauntSelector()

    override fun onInitialized(tree: Tree) {
        super.onInitialized(tree)
        // The list fills the whole row, so an empty title keeps OneConfig from squeezing a label beside it
        // (a missing title would fall back to the property ID).
        getProperty("taunts").title = Component.empty()
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
