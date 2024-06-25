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
import cc.polyfrost.oneconfig.utils.dsl.mc
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil
import me.waffles.autotaunt.AutoTaunt
import me.waffles.autotaunt.element.MacroListOption
import me.waffles.autotaunt.element.WrappedMacro
import java.lang.reflect.Field
import java.util.*
import java.util.stream.Collectors


object ModConfig : Config(Mod(AutoTaunt.NAME, ModType.UTIL_QOL), AutoTaunt.MODID + ".json") {

    @KeyBind(name = "AutoTaunt keybind", size = OptionSize.DUAL)
    var keyBind = OneKeyBind(UKeyboard.KEY_L)

    @Dropdown(name = "Chat type", options = ["Default", "All chat", "Party chat", "Shout", "Smart"], size = OptionSize.DUAL)
    var value = 0

    @CustomOption
    var entries: ArrayList<Macro> = ArrayList()

    @Exclude
    private val rand = Random()

    @Exclude
    private var tempList = ArrayList<String>()

    @Exclude
    private val games = listOf(
        "BEDWARS_FOUR_FOUR",
        "BEDWARS_EIGHT_TWO",
        "BEDWARS_FOUR_THREE",
        "BEDWARS_FOUR_FOUR",
        "BEDWARS_CASTLE",
        "BEDWARS_FOUR_FOUR",
        "BEDWARS_TWO_FOUR",
        "DUELS_UHC_FOUR",
        "DUELS_SW_DOUBLES",
        "DUELS_UHC_DOUBLES",
        "DUELS_OP_DOUBLES",
        "DUELS_MW_DOUBLES",
        "DUELS_BRIDGE_DOUBLES",
        "DUELS_BRIDGE_THREES",
        "DUELS_BRIDGE_FOUR",
        "DUELS_BRIDGE_2V2V2V2",
        "DUELS_BRIDGE_3V3V3V3",
        "DUELS_CAPTURE_THREES"
    )

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
        var text: String
        do {
            text = filteredEntries[rand.nextInt(filteredEntries.size)].text
        } while (tempList.contains(text) && filteredEntries.size > 1)

        tempList.add(text)

        if (tempList.size == filteredEntries.size) {
            tempList.clear()
        }

        val locraw = LocrawUtil.INSTANCE.locrawInfo
        if (value == 4 && HypixelUtils.INSTANCE.isHypixel()) {
            return if (locraw == null || mc.thePlayer.capabilities.allowFlying || !AutoTaunt.ingame) {
                "/ac $text"
            } else if (isTeamGame(locraw)) {
                "/shout $text"
            } else {
                "/ac $text"
            }
        } else if (value == 4 && !HypixelUtils.INSTANCE.isHypixel()) {
            return text
        } else {
            return when (value) {
                0 -> ""
                1 -> "/ac "
                2 -> "/pc "
                else -> "/shout "
            } + text
        }
    }

    private fun isTeamGame(info: LocrawInfo): Boolean {
        for (game in games) {
            if (info.gameMode.contains(game)) return true
        }
        return false
    }

    private fun test() {
        taunt()?.let { UChat.say(it) }
    }

}