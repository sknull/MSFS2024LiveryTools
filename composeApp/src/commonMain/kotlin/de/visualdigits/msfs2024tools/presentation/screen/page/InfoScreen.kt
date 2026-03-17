package de.visualdigits.msfs2024tools.presentation.screen.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.unit.dp
import be.digitalia.compose.htmlconverter.HtmlStyle
import be.digitalia.compose.htmlconverter.htmlToAnnotatedString
import de.visualdigits.generated.AppConfig
import de.visualdigits.msfs2024tools.presentation.screen.ScreenFrame
import de.visualdigits.msfs2024tools.presentation.style.ProjectStyle.ShapeContainer
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.title_info
import org.jetbrains.compose.resources.stringResource
import java.time.LocalDate
import java.time.temporal.ChronoField


@Composable
fun InfoScreen(
    modifier: Modifier = Modifier,
) {
    ScreenFrame(
        modifier = modifier,
        containerModifier = Modifier
            .fillMaxSize()
            .padding(start = 100.dp),
        contentModifier = Modifier
            .fillMaxSize(fraction = 0.97f)
            .clip(ShapeContainer)
            .background(Color.Black.copy(alpha = 0.4f)),
        label = stringResource(Res.string.title_info)
    ) {
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

        Text(
            modifier = modifier
                .padding(16.dp),
            text = html,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
