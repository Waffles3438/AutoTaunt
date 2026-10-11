package me.waffles.autotaunt.config

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TauntSelectorTest {
    @Test fun `each distinct enabled taunt is used once per cycle`() {
        val selector = TauntSelector(Random(42))
        val entries = listOf(Macro("a"), Macro("b"), Macro("c"))
        repeat(5) {
            assertEquals(setOf("a", "b", "c"), List(3) { selector.next(entries) }.toSet())
        }
    }

    @Test fun `duplicate messages cannot trap selection in a loop`() {
        val selector = TauntSelector(Random(42))
        val entries = listOf(Macro("a"), Macro("a"), Macro("b"))
        repeat(5) { assertEquals(setOf("a", "b"), List(2) { selector.next(entries) }.toSet()) }
    }

    @Test fun `empty and disabled entries are ignored`() {
        val selector = TauntSelector()
        assertNull(selector.next(listOf(Macro(""), Macro("  "), Macro("off", false))))
        repeat(3) { assertEquals("on", selector.next(listOf(Macro("off", false), Macro("on")))) }
    }

    @Test fun `removing and disabling taunts mid-cycle keeps selection live`() {
        val selector = TauntSelector(Random(42))
        val entries = listOf(Macro("a"), Macro("b"))
        val first = selector.next(entries)
        val remaining = entries.filter { it.text != first }
        repeat(5) { assertEquals(remaining.single().text, selector.next(remaining)) }
        assertNull(selector.next(emptyList()))
        assertEquals("new", selector.next(listOf(Macro("new"))))
    }
}
