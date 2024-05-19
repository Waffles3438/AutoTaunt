package me.waffles.autotaunt.element

import cc.polyfrost.oneconfig.gui.elements.BasicButton
import cc.polyfrost.oneconfig.utils.InputHandler
import cc.polyfrost.oneconfig.utils.color.ColorPalette
import me.waffles.autotaunt.AutoTaunt
import me.waffles.autotaunt.config.Macro

@Suppress("UnstableAPIUsage")
class WrappedMacro(
    val macro: Macro
) {
    private val removeButton = BasicButton(32, 32, AutoTaunt.MINUS_ICON, BasicButton.ALIGNMENT_CENTER, ColorPalette.PRIMARY_DESTRUCTIVE)
    private val checkbox = MacroCheckbox(macro)
    private val textField = MacroTextField(macro)

    init {
        removeButton.setClickAction {
            MacroListOption.willBeRemoved = this
        }
    }

    fun draw(vg: Long, x: Float, y: Float, inputHandler: InputHandler) {
        removeButton.draw(vg, x, y, inputHandler)
        checkbox.draw(vg, x + 58, y, inputHandler)
        textField.draw(vg, x + 96, y, inputHandler)
    }

    fun keyTyped(key: Char, keyCode: Int) = textField.isKeyTyped(key, keyCode)

}