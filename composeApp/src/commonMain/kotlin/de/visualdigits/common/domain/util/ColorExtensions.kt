package de.visualdigits.common.domain.util

import androidx.compose.ui.graphics.Color
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Returns a copy of this color with the given [hue], [saturation] and [value] values.
 *
 * hue 0 - 360
 * saturation 0.0f - 1.0f
 * value 0.0f - 1.0f
 *
 */
fun Color.copy(hue: Int? = null, saturation: Float? = null, value: Float? = null): Color {
    val (h, s, v) = toHsv()

    return fromHsv(hue?:h, saturation?:s, value?:v)
}

fun Color.hue(shift: Int): Color {
    val (h, s, v) = toHsv()

    return fromHsv(((h + shift) % 360), s, v)
}

fun Color.saturation(factor: Float): Color {
    val (h, s, v) = toHsv()

    return fromHsv(h, (s * factor).coerceIn(0.0f, 1.0f), v)
}

fun Color.value(factor: Float): Color {
    val (h, s, v) = toHsv()

    val f = v * factor
    return fromHsv(h, s, f.coerceIn(0.0f, 1.0f))
}

/**
 * Returns this color expressed as hue, saturation, value
 *
 * hue 0 - 360
 * saturation 0.0f - 1.0f
 * value 0.0f - 1.0f
 */
fun Color.toHsv(): Triple<Int, Float, Float> {
    val r = red.toDouble()
    val g = green.toDouble()
    val b = blue.toDouble()
    val min = min(r, min(g, b))
    val max = max(r, max(g, b))
    val delta = max - min
    val s: Double
    var h: Double
    if (max == 0.0) {
        s = 0.0
        h = 0.0
    } else {
        s = delta / max
        h = if (r == max) {
            (g - b) / delta
        } else if (g == max) {
            2 + (b - r) / delta
        } else {
            4 + (r - g) / delta
        }
        h *= 60.0
        if (h < 0) {
            h += 360.0
        }
        if (java.lang.Double.isNaN(h)) {
            h = 0.0
        }
    }

    return Triple(h.toInt(), s.toFloat(), max.toFloat())
}

/**
 * Returns a Color representing the given [hue], [saturation] and [value] values.
 *
 * hue 0 - 360
 * saturation 0.0f - 1.0f
 * value 0.0f - 1.0f
 */
fun fromHsv(hue: Int, saturation: Float, value: Float): Color {
    val h = (hue / 360.0)
    val s = saturation.toDouble()
    val v = value.toDouble()
    val components = (if (s == 0.0) {
        listOf(v, v, v)
    } else {
        val varH = h * 6
        val varI = floor(varH)
        val var1 = v * (1 - s)
        val var2 = v * (1 - s * (varH - varI))
        val var3 = v * (1 - s * (1 - (varH - varI)))

        when (varI) {
            0.0 -> listOf(v, var3, var1)
            1.0 -> listOf(var2, v, var1)
            2.0 -> listOf(var1, v, var3)
            3.0 -> listOf(var1, var2, v)
            4.0 -> listOf(var3, var1, v)
            else -> listOf(v, var1, var2)
        }
    }).map { 255.coerceAtMost((it * 255.0).roundToInt()) }

    return Color(components[0], components[1], components[2])
}
