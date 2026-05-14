package de.visualdigits.msfs2024tools.presentation.style

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.style.TextDecoration
import de.visualdigits.common.domain.model.StringResourceEnumerable
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.keyfactory.KeyFactory
import de.visualdigits.common.presentation.components.StudioClockColors
import de.visualdigits.common.presentation.components.defaultStudioClockColors
import de.visualdigits.compose.resources.Res
import de.visualdigits.compose.resources.background_lufthansa_100_baked
import org.jetbrains.compose.resources.DrawableResource

enum class DisplayThemeEnum(
    override val uiText: UiText,
    override val drawableResourceId: DrawableResource?,
    val colorScheme: ColorScheme,
    val textLinkStyles:TextLinkStyles,
    val textColor: Color,
    val studioClockColors: StudioClockColors,
    val backgroundImage: DrawableResource,
    val isDark: Boolean // consider as dark theme for android
) : StringResourceEnumerable<DisplayThemeEnum> {

    //
    // Remember to configure the fitting laf in
    // de/visualdigits/newshomereader/DisplayThemeEnumExtensions.kt
    //

    DARK(
        isDark = true,
        backgroundImage = Res.drawable.background_lufthansa_100_baked,
        uiText = UiText.DynamicString("Dark"),
        drawableResourceId = null,
        colorScheme = darkColorScheme(
            primary = Color(0xFF000000),
            onPrimary = Color(0xFFFFFFFF),
            onPrimaryContainer = Color(0xFFFFFFFF),

            secondary = Color(0xFF313030),
            onSecondary = Color(0xFFFFFFFF),

            secondaryContainer = Color(0xFFE1E1E1),
            onSecondaryContainer = Color(0xFF9A9A9A),

            background = Color(0xFF000000),
            onBackground = Color(0xFFFFFFFF),

            surface = Color(0xFF000000),
            onSurface = Color(0xff3b84eb), // deco color

            inverseSurface = Color(0xFFFFFFFF),
            surfaceContainer = Color(0xFFFFFFFF),
            surfaceContainerHigh = Color.Transparent,
            surfaceContainerLow = Color.Transparent,
            surfaceContainerLowest = Color(0xFF373737),
            surfaceDim = Color(0xFF393939),

            error = Color(0xffff002a),
            onError = Color(0xFFFFFFFF),
            errorContainer = Color(0xffff002a),
            onErrorContainer = Color(0xFFFFFFFF),

            outline = Color(0xFFFFFFFF),

            primaryFixed = Color(0xAA000000)
        ),
        textLinkStyles = TextLinkStyles(
            style = SpanStyle(
                color = Color(0xff3b84eb),
                textDecoration = TextDecoration.Underline
            )
        ),
        textColor = Color(0xFFFFFFFF),
        studioClockColors = defaultStudioClockColors
    ),
    ;

    override fun toString(): String = name.lowercase()

    companion object : KeyFactory<DisplayThemeEnum> {

        override val options: List<Triple<DisplayThemeEnum, UiText?, DrawableResource?>> = entries.map { e -> Triple(e, e.uiText, e.drawableResourceId) }

        override fun fromString(value: String?): DisplayThemeEnum? {
            return entries.find { e -> e.name == value }
        }

        override fun fromValue(value: Any?): DisplayThemeEnum? {
            return when (value) {
                is String -> fromString(value)
                is DisplayThemeEnum -> value
                else -> null
            }
        }

        override fun stringValue(value: Any?): String? {
            return (value as? DisplayThemeEnum)?.name?:value?.toString()
        }
    }
}
