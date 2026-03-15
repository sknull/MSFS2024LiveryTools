package de.visualdigits.msfs2024tools.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.unit.dp
import be.digitalia.compose.htmlconverter.HtmlStyle
import be.digitalia.compose.htmlconverter.htmlToAnnotatedString
import de.visualdigits.common.presentation.components.FlexibleTextButton
import de.visualdigits.common.presentation.components.bevelBorder
import de.visualdigits.generated.AppConfig
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ShapeButton
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ShapeContainer
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.ok
import msfs2024liverytools.composeapp.generated.resources.title_info
import org.jetbrains.compose.resources.stringResource
import java.time.LocalDate
import java.time.temporal.ChronoField


@Composable
fun Msfs2024InfoDialog(
    showInfoDialog: Boolean,
    setShowInfoDialog: (Boolean) -> Unit,
) {
    if (showInfoDialog) {
        val linkColor = MaterialTheme.colorScheme.primary

        val html = remember(linkColor) {
            htmlToAnnotatedString(
                html = """
                        <h1>MSFS 2024 Livery Tools</h1>
                        <h3>Version ${AppConfig.VERSION}</h3>
                        <br/>
                        <div>© ${LocalDate.now().get(ChronoField.YEAR)} by Stephan Knull.<div>
                        <br/>
                        <div>Github project page: <a href="https://github.com/sknull/MSFS2024LiveryTools">https://github.com/sknull/MSFS2024LiveryTools</a></div>
                        <div>Flightsim.to page: <a href="https://flightsim.to/addon/105878/msfs-2024-livery-tools">https://flightsim.to/addon/105878/msfs-2024-livery-tools</a></div>
                        """.trimIndent(),
                style = HtmlStyle(
                    textLinkStyles = TextLinkStyles(style = SpanStyle(color = linkColor)),
                    isTextColorEnabled = true
                )
            )
        }

        AlertDialog(
            modifier = Modifier
                .border(1.dp, Color(0xFF333333), ShapeContainer)
                .padding(0.dp),
            shape = ShapeContainer,
            containerColor = Color.Black.copy(alpha = 0.8f),
            textContentColor = Color.White,
            title = {
                Text(
                    text = stringResource(Res.string.title_info),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Text(
                    text = html,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                FlexibleTextButton(
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Hand),
                    onClick = { setShowInfoDialog(false) },
                    buttonShape = ShapeButton,
                    buttonColor = Color.Gray
                ) {
                    Text(
                        text = stringResource(Res.string.ok),
                        color = Color.White
                    )
                }
            },
            onDismissRequest = { setShowInfoDialog(false) }
        )
    }
}
