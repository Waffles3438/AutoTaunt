package me.waffles.autotaunt.config

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TmpSendLimiterCheck {
    @Test fun `worst case key mashing never reaches the server kick line`() {
        var now = 0L
        val limiter = SendLimiter { now }
        var server = 0 // vanilla counter: +20 per message, -1 per tick, kick above 200
        var peak = 0
        var sent = 0
        repeat(400) { tick -> // 20 seconds of one press per tick
            if (server > 0) server--
            now = tick * 50_000_000L
            if (limiter.canSend()) { limiter.record(); server += 20; sent++ }
            peak = maxOf(peak, server)
        }
        assertTrue(peak <= 200, "peak=$peak")
        println("peak=$peak sent=$sent in 20s")
    }

    @Test fun `burst of nine then one per second`() {
        var now = 0L
        val limiter = SendLimiter { now }
        var burst = 0
        repeat(11) { i -> now = i * 50_000_000L; if (limiter.canSend()) { limiter.record(); burst++ } }
        assertEquals(9, burst)
        now += 1_000_000_000L
        assertTrue(limiter.canSend())
    }
}
