package me.waffles.autotaunt.element

import org.polyfrost.oneconfig.api.config.v1.annotations.Option

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Option(display = MacroListOption::class)
annotation class TauntsOption(
    val title: String = "Taunts",
    val description: String = "",
    val category: String = "General",
    val subcategory: String = "General"
)
