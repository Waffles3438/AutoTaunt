package me.waffles.autotaunt

import cc.polyfrost.oneconfig.utils.JsonUtils
import cc.polyfrost.oneconfig.utils.NetworkUtils
import cc.polyfrost.oneconfig.utils.dsl.runAsync
import com.google.gson.JsonObject
import java.util.regex.Pattern

object PatternHandler {
    var gameEnd = ArrayList<Pattern>()

    val regex = NetworkUtils.getJsonElement("https://data.woverflow.cc/regex.json").getAsJsonObject();

    fun initialize() {
        runAsync {
            try {
                regex
                if (regex != null) {
                    processJson(regex)
                    return@runAsync
                }
                val gotten = NetworkUtils.getString("https://data.woverflow.cc/regex.json")
                if (gotten != null) {
                    processJson(JsonUtils.parseString(gotten).asJsonObject)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun processJson(jsonObject: JsonObject) {
        for (element in jsonObject.getAsJsonArray("game_end")) {
            gameEnd.add(Pattern.compile(element.asString))
        }
    }

}