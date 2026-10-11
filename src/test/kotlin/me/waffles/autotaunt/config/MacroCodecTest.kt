package me.waffles.autotaunt.config

import kotlin.test.Test
import kotlin.test.assertEquals

class MacroCodecTest {
    @Test fun `persistence preserves enabled state quotes unicode and newlines`() {
        val macros = listOf(Macro("quoted \"text\"\\path\nnext line — hello"), Macro("disabled", false), Macro(""))
        assertEquals(macros, MacroCodec.decode(MacroCodec.encode(macros)))
    }

    @Test fun `a malformed record does not discard valid taunts`() {
        val valid = Macro("valid", false)
        assertEquals(listOf(valid), MacroCodec.decode(arrayOf("{broken", *MacroCodec.encode(listOf(valid)))))
    }

    @Test fun `null fields use safe defaults`() {
        assertEquals(listOf(Macro()), MacroCodec.decode(arrayOf("{\"text\":null,\"enabled\":null}")))
    }
}
