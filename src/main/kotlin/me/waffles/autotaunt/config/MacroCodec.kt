package me.waffles.autotaunt.config

import com.google.gson.Gson
import com.google.gson.JsonObject

/** OneConfig can persist arrays of strings; each string holds a complete taunt record. */
object MacroCodec {
    private val gson = Gson()

    fun encode(macros: List<Macro>): Array<String> = macros.map(gson::toJson).toTypedArray()

    fun decode(values: Array<String>): List<Macro> = values.mapNotNull { value ->
        runCatching {
            val json = gson.fromJson(value, JsonObject::class.java) ?: return@mapNotNull null
            Macro(
                json.get("text")?.takeUnless { it.isJsonNull }?.asString.orEmpty(),
                json.get("enabled")?.takeUnless { it.isJsonNull }?.asBoolean ?: true
            )
        }.getOrNull()
    }
}
