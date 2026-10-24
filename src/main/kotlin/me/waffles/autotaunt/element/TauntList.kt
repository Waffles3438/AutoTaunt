package me.waffles.autotaunt.element

import org.polyfrost.oneconfig.api.config.v1.annotations.Option

/** A [org.polyfrost.oneconfig.api.config.v1.annotations.TextList] that spans the whole settings row. */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Option(display = FullWidthTextList::class)
annotation class TauntList(
    val title: String = "Taunts",
    val category: String = "General",
    val subcategory: String = "General",
    val placeholder: String = "Enter a taunt",
    val addText: String = "Add taunt"
)
