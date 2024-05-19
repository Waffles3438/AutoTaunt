package me.waffles.autotaunt.element

import cc.polyfrost.oneconfig.config.elements.BasicOption
import cc.polyfrost.oneconfig.gui.elements.BasicButton
import cc.polyfrost.oneconfig.utils.InputHandler
import cc.polyfrost.oneconfig.utils.color.ColorPalette
import me.waffles.autotaunt.AutoTaunt
import me.waffles.autotaunt.config.Macro

@Suppress("UnstableAPIUsage")
object MacroListOption : BasicOption(null, null, "", "", "General", "", 2) {

    private val addButton = BasicButton(32, 32, AutoTaunt.PLUS_ICON, BasicButton.ALIGNMENT_CENTER, ColorPalette.PRIMARY)
    var wrappedMacros: MutableList<WrappedMacro> = ArrayList()
    var willBeRemoved: WrappedMacro? = null

    init {
        addButton.setClickAction {
            wrappedMacros.add(WrappedMacro(Macro()))
        }
    }

    override fun getHeight() = wrappedMacros.size * 48 + 32

    override fun draw(vg: Long, x: Int, y: Int, inputHandler: InputHandler) {
        var nextY = y

        for (option in wrappedMacros) {
            option.draw(vg, x.toFloat(), nextY.toFloat(), inputHandler)
            nextY += 48
        }

        addButton.draw(vg, x.toFloat(), nextY.toFloat(), inputHandler)

        checkWillBeRemoved()
    }

    private fun checkWillBeRemoved() {
        val macro = willBeRemoved ?: return
        wrappedMacros.remove(macro)
        willBeRemoved = null
    }

    override fun keyTyped(key: Char, keyCode: Int) {
        wrappedMacros.any { it.keyTyped(key, keyCode) }
    }

}