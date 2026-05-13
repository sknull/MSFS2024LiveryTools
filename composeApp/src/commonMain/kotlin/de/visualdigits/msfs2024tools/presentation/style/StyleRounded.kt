package de.visualdigits.msfs2024tools.presentation.style

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.visualdigits.compose.resources.Res
import de.visualdigits.compose.resources.background_lufthansa_100_baked


abstract class StyleRounded {

    val BackgroundImageMain = Res.drawable.background_lufthansa_100_baked
    val BackgroundArrowsScale = 2.5f
    val BackgroundArrowsTranslationX = 0.3f
    val BackgroundArrowsTranslationy = 0.1f

    val ColorPrimary = Color(0xff3b84eb)
    val ColorOnPrimary = Color.White
    val ColorSecondary = Color.White

    val ColorTabBackground = Color.Black.copy(alpha = 0.2f)
    val ColorCardBackground = Color.Black.copy(alpha = 0.2f)

    val ColorTabSelected = ColorSecondary
    val ColorTabUnselected = ColorPrimary

    val ColorUnfocused = ColorPrimary
    val ColorFocused = ColorSecondary

    val ColorText = ColorSecondary
    val ColorIcon = ColorSecondary

    val ColorButton = Color(0xff1e2125)

    val ColorSchemeMsfs2024Tools = darkColorScheme(
        primary = ColorPrimary,
        onPrimary = ColorOnPrimary,
        error = Color(0xffff002a),
        surfaceDim = Color(0xffaaaaaa)
    )

    val MsfsTabButtonSelectedBgColor = Color(0xff193866)
    val MsfsTabButtonSelectedFgColor = ColorPrimary
    val MsfsTabButtonUnselectedBrush = Brush.linearGradient(listOf(Color(0xff2e68ba), Color(0xff133c7b)))

    val ShapeButton = RoundedCornerShape(6.dp)
    val ShapeContainer = RoundedCornerShape(10.dp)
    val PaddingContainer = 8.dp

    val SpaceBetweenComponents = 8.dp

    val TypographyMsfs2024Tools = Typography(
        titleLarge = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp,
            color = ColorText
        ),
        titleSmall = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp,
            color = ColorText
        ),

        headlineLarge = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Black,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.0.sp,
            color = ColorText
        ),
        headlineSmall = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.0.sp,
            color = ColorText
        ),

        bodyMedium = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.2.sp,
            color = ColorText
        ),
        bodySmall = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp,
            color = ColorText
        ),

        displaySmall = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            letterSpacing = 0.0.sp,
            color = ColorText
        )
    )
}
