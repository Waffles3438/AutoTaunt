package me.waffles.autotaunt.element

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.waffles.autotaunt.config.Macro
import me.waffles.autotaunt.config.MacroCodec
import me.waffles.autotaunt.config.ModConfig
import org.polyfrost.oneconfig.api.config.v1.Property
import org.polyfrost.oneconfig.api.config.v1.Visualizer
import java.util.function.Predicate

/** The custom taunt list renders directly in OneConfig v1's Compose settings page. */
class MacroListOption : Visualizer {
    @Composable
    override fun visualize(prop: Property<*>) {
        @Suppress("UNCHECKED_CAST")
        val property = prop as Property<Array<String>>
        var macros by remember(property) { mutableStateOf(MacroCodec.decode(property.get() ?: emptyArray())) }
        DisposableEffect(property) {
            val listener = Predicate<Array<String>> { macros = MacroCodec.decode(it); false }
            property.addCallback(listener)
            onDispose { property.removeCallback(listener) }
        }
        fun update(values: List<Macro>) {
            property.set(MacroCodec.encode(values))
            ModConfig.save()
        }
        MaterialTheme(colors = darkColors()) {
            Surface {
                Column(Modifier.width(420.dp).padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (macros.isEmpty()) Text("Add a taunt to get started.")
                    else LazyColumn(Modifier.heightIn(max = 300.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        itemsIndexed(macros) { index, macro ->
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Checkbox(macro.enabled, { enabled ->
                                    update(macros.mapIndexed { i, entry -> if (i == index) entry.copy(enabled = enabled) else entry })
                                })
                                OutlinedTextField(macro.text, { text ->
                                    update(macros.mapIndexed { i, entry -> if (i == index) entry.copy(text = text) else entry })
                                }, modifier = Modifier.weight(1f), singleLine = true, placeholder = { Text("Taunt") })
                                Button(onClick = { update(macros.filterIndexed { i, _ -> i != index }) }) { Text("−") }
                            }
                        }
                    }
                    Button(onClick = { update(macros + Macro()) }) { Text("Add taunt") }
                }
            }
        }
    }
}
