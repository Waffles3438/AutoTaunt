package me.waffles.autotaunt.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.CustomOption
import cc.polyfrost.oneconfig.config.annotations.Dropdown
import cc.polyfrost.oneconfig.config.annotations.Exclude
import cc.polyfrost.oneconfig.config.annotations.KeyBind
import cc.polyfrost.oneconfig.config.core.ConfigUtils
import cc.polyfrost.oneconfig.config.core.OneKeyBind
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.config.data.OptionSize
import cc.polyfrost.oneconfig.config.elements.BasicOption
import cc.polyfrost.oneconfig.config.elements.OptionPage
import cc.polyfrost.oneconfig.libs.universal.UChat
import cc.polyfrost.oneconfig.libs.universal.UKeyboard
import me.waffles.autotaunt.AutoTaunt
import me.waffles.autotaunt.element.MacroListOption
import me.waffles.autotaunt.element.WrappedMacro
import java.lang.reflect.Field
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList

object ModConfig : Config(Mod(AutoTaunt.NAME, ModType.UTIL_QOL), AutoTaunt.MODID + ".json") {

    @KeyBind(name = "AutoTaunt keybind", size = OptionSize.DUAL)
    var keyBind = OneKeyBind(UKeyboard.KEY_L)

    @Dropdown(name = "Chat type", options = ["Default", "All chat", "Party chat", "Shout"], size = OptionSize.DUAL)
    var value = 0

    @CustomOption
    var entries: ArrayList<Macro> = ArrayList()

    @Exclude
    private val rand = Random()

    @Exclude
    private var prev = ""

    init {
        initialize()
        registerKeyBind(keyBind) { test() }
    }

    override fun getCustomOption(
        field: Field,
        annotation: CustomOption,
        page: OptionPage,
        mod: Mod,
        migrate: Boolean
    ): BasicOption {
        val option = MacroListOption
        ConfigUtils.getSubCategory(page, "General", "").options.add(option)
        return option
    }

    override fun load() {
        super.load()
        MacroListOption.wrappedMacros = entries.mapTo(mutableListOf()) { macro ->
            WrappedMacro(macro)
        }
    }

    override fun save() {
        entries = ArrayList(MacroListOption.wrappedMacros.map { wrapped ->
            wrapped.macro
        }.toList())
        super.save()
    }

    private fun taunt(): String? {
        val filteredEntries = entries.stream().filter { it.enabled }.collect(Collectors.toList())
        if (filteredEntries.isEmpty()) return null
        var random = rand.nextInt(filteredEntries.size)
        var text = filteredEntries[random].text
        while (text == prev && filteredEntries.size != 1) {
            random = rand.nextInt(filteredEntries.size)
            text = filteredEntries[random].text
        }
        prev = text

        return when (value) {
            0 -> ""
            1 -> "/ac "
            2 -> "/pc "
            else -> "/shout "
        } + text
    }

    private fun test() {
        taunt()?.let { UChat.say(it) }
    }

}