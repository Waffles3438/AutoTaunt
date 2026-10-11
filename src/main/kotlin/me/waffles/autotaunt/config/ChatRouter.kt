package me.waffles.autotaunt.config

object ChatRouter {
    private val teamGames = setOf(
        "BEDWARS_FOUR_FOUR", "BEDWARS_EIGHT_TWO", "BEDWARS_FOUR_THREE", "BEDWARS_CASTLE", "BEDWARS_TWO_FOUR",
        "DUELS_UHC_FOUR", "DUELS_SW_DOUBLES", "DUELS_UHC_DOUBLES", "DUELS_OP_DOUBLES", "DUELS_MW_DOUBLES",
        "DUELS_BRIDGE_DOUBLES", "DUELS_BRIDGE_THREES", "DUELS_BRIDGE_FOUR", "DUELS_BRIDGE_2V2V2V2",
        "DUELS_BRIDGE_3V3V3V3", "DUELS_CAPTURE_THREES"
    )

    fun route(text: String, chatType: Int, hypixel: Boolean, mode: String?, canFly: Boolean, inGame: Boolean): String {
        val prefix = when (chatType) {
            1 -> "/ac "
            2 -> "/pc "
            3 -> "/shout "
            4 -> when {
                !hypixel -> ""
                mode == null || canFly || !inGame -> "/ac "
                teamGames.any { mode.contains(it, ignoreCase = true) } -> "/shout "
                else -> "/ac "
            }
            else -> ""
        }
        return prefix + text
    }
}
