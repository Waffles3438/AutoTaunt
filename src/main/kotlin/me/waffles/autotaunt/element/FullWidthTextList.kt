package me.waffles.autotaunt.element

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import org.polyfrost.oneconfig.api.config.v1.Property
import org.polyfrost.oneconfig.api.config.v1.Visualizer
import org.polyfrost.oneconfig.internal.ui.api.settings.TextListOptionData
import org.polyfrost.oneconfig.internal.ui.components.settings.LocalOptionWidth
import org.polyfrost.oneconfig.internal.ui.components.settings.TextListOption

/**
 * OneConfig's own text list, stretched to the full width of the settings row instead of its fixed 300dp.
 * All list behaviour (add, edit, remove, drag to reorder) comes from the built-in [TextListOption].
 */
class FullWidthTextList : Visualizer {
    @Composable
    override fun visualize(prop: Property<*>) {
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            val width = if (constraints.hasBoundedWidth) maxWidth else LocalOptionWidth.current
            CompositionLocalProvider(LocalOptionWidth provides width) {
                TextListOption(TextListOptionData(prop))
            }
        }
    }
}
