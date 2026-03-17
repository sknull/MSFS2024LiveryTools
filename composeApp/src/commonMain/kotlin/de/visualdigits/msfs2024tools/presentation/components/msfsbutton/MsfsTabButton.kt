package de.visualdigits.msfs2024tools.presentation.components.msfsbutton

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.visualdigits.common.presentation.components.FlexibleTextButton
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.MsfsTabButtonSelectedBgColor
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.MsfsTabButtonSelectedFgColor
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.MsfsTabButtonUnselectedBrush
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ShapeButton
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.icon_checkmark
import org.jetbrains.compose.resources.painterResource

@Composable
fun MsfsTabButton(
    modifier: Modifier = Modifier,
    text: String,
    width: Dp = 160.dp,
    height: Dp = 50.dp,
    paddingStart: Dp = 8.dp,
    paddingTop: Dp = 5.dp,
    paddingEnd: Dp = 0.dp,
    paddingBottom: Dp = 0.dp,
    onClick: () -> Unit,
    selected: Boolean = false
) {
    if (selected) {
        Box(
            modifier = modifier
                .clip(ShapeButton)
                .background(MsfsTabButtonSelectedBgColor)
                .width(width)
                .height(height),
        ) {
            Image(
                painter = painterResource(Res.drawable.icon_checkmark),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 4.dp, bottom = 4.dp)
                    .align(Alignment.BottomEnd)
            )

            val buttonShape = MsfsTabButtonShape(radius = 6.dp, cutSizeX = 0.3f, cutSizeY = 0.6f)
            FlexibleTextButton(
                text = text,
                width = width,
                height = height,
                outerPaddingValues = PaddingValues(0.dp),
                contentAlignment = Alignment.TopStart,
                paddingStart = paddingStart,
                paddingTop = paddingTop,
                paddingEnd = paddingEnd,
                paddingBottom = paddingBottom,
                onClick = onClick,
                modifier = Modifier
                    .clip(ShapeButton)
                    .pointerHoverIcon(PointerIcon.Hand),
                buttonColor = MsfsTabButtonSelectedFgColor,
                buttonShape = buttonShape,
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            )
        }
    } else {
        FlexibleTextButton(
            text = text,
            width = width,
            height = height,
            outerPaddingValues = PaddingValues(0.dp),
            contentAlignment = Alignment.TopStart,
            paddingStart = paddingStart,
            paddingTop = paddingTop,
            paddingEnd = paddingEnd,
            paddingBottom = paddingBottom,
            onClick = onClick,
            modifier = Modifier
                .clip(ShapeButton)
                .pointerHoverIcon(PointerIcon.Hand),
            buttonBrush = MsfsTabButtonUnselectedBrush,
            buttonShape = ShapeButton,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        )
    }
}
