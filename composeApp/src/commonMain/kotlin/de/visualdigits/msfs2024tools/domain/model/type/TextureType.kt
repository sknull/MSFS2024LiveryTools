package de.visualdigits.msfs2024tools.domain.model.type

import de.visualdigits.common.domain.model.Enumerable
import de.visualdigits.common.domain.model.configuration.KeyFactory

enum class TextureType(
    val bitmapSlot: String,
    val forceNoAlpha: Boolean,
): Enumerable<TextureType> {

    ALBD(
        bitmapSlot = "MTL_BITMAP_DECAL0",
        forceNoAlpha = false
    ),

    COMP(
        bitmapSlot = "MTL_BITMAP_METAL_ROUGH_AO",
        forceNoAlpha = true
    ),

    DECAL(
        bitmapSlot = "MTL_BITMAP_DECAL0",
        forceNoAlpha = false
    ),

    NORM(
        bitmapSlot = "MTL_BITMAP_NORMAL",
        forceNoAlpha = false
    )
    ;

    companion object : KeyFactory<TextureType> {

        override fun fromString(value: String?): TextureType? {
            return TextureType.entries.find { e -> e.name == value }
        }

        override fun stringValue(value: Any?): String? = (value as? TextureType)?.name
    }
}
