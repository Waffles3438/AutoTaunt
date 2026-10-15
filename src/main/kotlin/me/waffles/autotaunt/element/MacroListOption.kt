package me.waffles.autotaunt.element

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.waffles.autotaunt.config.Macro
import me.waffles.autotaunt.config.MacroCodec
import me.waffles.autotaunt.config.ModConfig
import org.polyfrost.oneconfig.api.config.v1.Properties
import org.polyfrost.oneconfig.api.config.v1.Property
import org.polyfrost.oneconfig.api.config.v1.Visualizer
import org.polyfrost.oneconfig.api.ui.v1.keybind.trackTextInputFocus
import org.polyfrost.oneconfig.internal.ui.components.IconButton
import org.polyfrost.oneconfig.internal.ui.components.Text
import org.polyfrost.oneconfig.internal.ui.components.settings.CheckboxIndicator
import org.polyfrost.oneconfig.internal.ui.themes.LocalTheme
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

        val theme = LocalTheme.current
        val onAdd by rememberUpdatedState({ update(macros + Macro()) })
        val addButton = remember(property) {
            Properties.simple(Runnable { onAdd() }).apply { addMetadata("text", "Add taunt") }
        }

        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (macros.isNotEmpty()) {
                LazyColumn(
                    Modifier.fillMaxWidth().heightIn(max = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(macros) { index, macro ->
                        BasicTextField(
                            value = macro.text,
                            onValueChange = { text ->
                                update(macros.mapIndexed { i, entry -> if (i == index) entry.copy(text = text) else entry })
                            },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = theme.textColor,
                                fontSize = 14.sp,
                                fontFamily = theme.typography.family,
                            ),
                            cursorBrush = SolidColor(theme.textColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .trackTextInputFocus()
                                .semantics { contentDescription = "Taunt ${index + 1}" }
                                .background(theme.componentBackground, theme.sideBarNavigationEntryShape)
                                .border(1.dp, theme.borderColor, theme.sideBarNavigationEntryShape)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            decorationBox = { innerTextField ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                ) {
                                    CheckboxIndicator(
                                        checked = macro.enabled,
                                        onToggle = {
                                            update(macros.mapIndexed { i, entry ->
                                                if (i == index) entry.copy(enabled = !entry.enabled) else entry
                                            })
                                        },
                                        modifier = Modifier.semantics {
                                            contentDescription = "Enable taunt ${index + 1}"
                                            stateDescription = if (macro.enabled) "Enabled" else "Disabled"
                                        },
                                    )
                                    Box(Modifier.weight(1f)) {
                                        if (macro.text.isEmpty()) Text("Taunt", color = theme.textColorSecondary)
                                        innerTextField()
                                    }
                                    IconButton(
                                        "trash",
                                        modifier = Modifier.size(20.dp).semantics {
                                            contentDescription = "Remove taunt ${index + 1}"
                                        },
                                        onClick = { update(macros.filterIndexed { i, _ -> i != index }) },
                                    )
                                }
                            },
                        )
                    }
                }
            }
            Visualizer.ButtonVisualizer().visualize(addButton)
        }
    }
}
