package de.visualdigits.msfs2024tools.presentation.tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ColorIcon
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.icon_arrow_forward_ios_24px
import msfs2024liverytools.composeapp.generated.resources.show_details_hint
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun RowScope.ItemIcon(modifier: Modifier) {
    Column(
        modifier = modifier
            .weight(1f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        Icon(
            painter = painterResource(Res.drawable.icon_arrow_forward_ios_24px),
            contentDescription = stringResource(Res.string.show_details_hint),
            tint = ColorIcon
        )
    }
}
