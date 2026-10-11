package me.waffles.autotaunt.config

import com.google.gson.JsonParser
import org.apache.logging.log4j.LogManager
import org.polyfrost.oneconfig.internal.legacy.InputConstants
import org.polyfrost.oneconfig.internal.legacy.KeyCodes
import java.nio.file.Files
import java.nio.file.Paths

/** Reads legacy data once, leaving the original OneConfig v0 file intact. */
object LegacyConfigMigration {
    private val logger = LogManager.getLogger("AutoTaunt")
    private val candidates = listOf(
        Paths.get("OneConfig", "config", "autotaunt.json"),
        Paths.get("config", "OneConfig", "config", "autotaunt.json")
    )

    fun import(config: ModConfig) {
        val path = candidates.firstOrNull(Files::isRegularFile) ?: return
        try {
            val json = Files.newBufferedReader(path).use { JsonParser().parse(it).asJsonObject }
            // Populate only after the entire legacy list has parsed successfully.
            val entries = json.getAsJsonArray("entries")?.map { element ->
                val entry = element.asJsonObject
                Macro(entry.get("text")?.asString ?: "", entry.get("enabled")?.asBoolean ?: true)
            }?.toTypedArray()
            val chatType = json.get("value")?.asInt?.coerceIn(0, 4)
            val legacyKeys = json.getAsJsonObject("keyBind")?.getAsJsonArray("keyBinds")
                ?.map { it.asInt }
            val mappedKeys = legacyKeys?.map { KeyCodes.fromLegacy(it) }
            if (entries != null) config.entries = MacroCodec.encode(entries.toList())
            if (chatType != null) config.value = chatType
            if (!mappedKeys.isNullOrEmpty() && mappedKeys.all { it != InputConstants.UNKNOWN }) {
                config.keyBind.keyCodes = mappedKeys.filter { it.type == InputConstants.Type.KEYBOARD }.map { it.value }.toIntArray()
                config.keyBind.mouseBtns = mappedKeys.filter { it.type == InputConstants.Type.MOUSE }.map { it.value }.toIntArray()
            } else if (!legacyKeys.isNullOrEmpty()) {
                logger.warn("Legacy keybind could not be mapped; rebind it in OneConfig")
            }
            logger.info("Imported legacy AutoTaunt settings from {}", path)
        } catch (exception: Exception) {
            logger.warn("Could not import legacy AutoTaunt settings from {}", path, exception)
        }
    }

}
