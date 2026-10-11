package me.waffles.autotaunt

import com.google.gson.JsonParser
import org.apache.logging.log4j.LogManager
import java.net.HttpURLConnection
import java.net.URI
import java.util.regex.Pattern
import kotlin.concurrent.thread

object PatternHandler {
    private val logger = LogManager.getLogger("AutoTaunt")
    private val fallback = listOf(
        "^\\s*VICTORY!\\s*$", "^\\s*DEFEAT!\\s*$", "^\\s*GAME OVER!\\s*$",
        ".*WINNER!.*", ".*Winner:.*", ".*Winners:.*"
    ).map(Pattern::compile)

    @Volatile
    var gameEnd: List<Pattern> = fallback
        private set

    fun initialize() {
        thread(name = "AutoTaunt-patterns", isDaemon = true) {
            var connection: HttpURLConnection? = null
            try {
                connection = URI("https://data.woverflow.cc/regex.json").toURL().openConnection() as HttpURLConnection
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                val patterns = connection.inputStream.bufferedReader().use { reader ->
                    JsonParser().parse(reader).asJsonObject.getAsJsonArray("game_end")
                        .map { Pattern.compile(it.asString) }
                }
                if (patterns.isNotEmpty()) gameEnd = patterns
            } catch (exception: Exception) {
                logger.warn("Could not refresh game-end patterns; using bundled defaults", exception)
            } finally {
                connection?.disconnect()
            }
        }
    }
}
