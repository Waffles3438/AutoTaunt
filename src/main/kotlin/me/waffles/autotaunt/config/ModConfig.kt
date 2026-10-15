package me.waffles.autotaunt.config

import me.waffles.autotaunt.AutoTaunt
import me.waffles.autotaunt.element.TauntsOption
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

    @TauntsOption
    var entries: Array<String> = emptyArray()

    val macros: List<Macro>
        get() = MacroCodec.decode(entries)

    @Include
    var legacyMigrationChecked = false

    private val selector = TauntSelector()

    override fun onInitialized(tree: Tree) {
        super.onInitialized(tree)
        // OneConfig ignores "" metadata and rejects replacing an existing title. Whitespace
        // clears the old title so empty rich text can hide the label without an ID fallback.
        getProperty("entries").apply {
            addMetadata("title", " ")
            title = Component.empty()
        }
        if (!legacyMigrationChecked) {
            LegacyConfigMigration.import(this)
            legacyMigrationChecked = true
            save()
        }
    }

    fun updateEntries(macros: List<Macro>) {
        getProperty("entries").setAs(MacroCodec.encode(macros))
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
