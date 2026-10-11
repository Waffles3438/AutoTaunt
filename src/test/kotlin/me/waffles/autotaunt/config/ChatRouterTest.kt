package me.waffles.autotaunt.config

import kotlin.test.Test
import kotlin.test.assertEquals

class ChatRouterTest {
    @Test fun `explicit chat modes retain their prefixes`() {
        listOf("hello", "/ac hello", "/pc hello", "/shout hello").forEachIndexed { mode, expected ->
            assertEquals(expected, ChatRouter.route("hello", mode, false, null, false, false))
        }
    }

    @Test fun `smart mode sends ordinary chat outside Hypixel`() {
        assertEquals("hello", ChatRouter.route("hello", 4, false, "BEDWARS_EIGHT_TWO", false, true))
    }

    @Test fun `smart mode shouts in active team games only`() {
        assertEquals("/shout hello", ChatRouter.route("hello", 4, true, "BEDWARS_EIGHT_TWO", false, true))
        assertEquals("/ac hello", ChatRouter.route("hello", 4, true, "BEDWARS_EIGHT_ONE", false, true))
        assertEquals("/shout hello", ChatRouter.route("hello", 4, true, "DUELS_BRIDGE_FOUR", false, true))
    }

    @Test fun `smart mode uses all chat in lobbies and for spectators`() {
        assertEquals("/ac hello", ChatRouter.route("hello", 4, true, "BEDWARS_EIGHT_TWO", true, true))
        assertEquals("/ac hello", ChatRouter.route("hello", 4, true, "BEDWARS_EIGHT_TWO", false, false))
        assertEquals("/ac hello", ChatRouter.route("hello", 4, true, null, false, true))
    }
}
