package me.waffles.autotaunt.config

import kotlin.random.Random

/** Chooses each distinct enabled message once per cycle, including after list edits. */
class TauntSelector(private val random: Random = Random.Default) {
    private val seen = mutableSetOf<String>()

    fun next(entries: List<Macro>): String? {
        val active = entries.filter { it.enabled && it.text.isNotBlank() }.map { it.text }.distinct()
        seen.retainAll(active.toSet())
        if (active.isEmpty()) return null
        var available = active.filterNot { it in seen }
        if (available.isEmpty()) {
            seen.clear()
            available = active
        }
        return available.random(random).also(seen::add)
    }
}
