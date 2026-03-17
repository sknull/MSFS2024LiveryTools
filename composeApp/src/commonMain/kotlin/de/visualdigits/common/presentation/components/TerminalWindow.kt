package de.visualdigits.common.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import de.visualdigits.common.domain.model.color
import de.visualdigits.msfs2024tools.domain.model.errorhandling.LogMessage


@Composable
fun TerminalWindow(
    modifier: Modifier = Modifier,
    shapeContainer: Shape,
    title: String,
    listData: () -> List<LogMessage>,
    backGroundColor: Color = Color.Black.copy(alpha = 0.8f),
    containerShape: Shape = RoundedCornerShape(8.dp),
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listData().size) {
        if (listData().isNotEmpty()) {
            listState.scrollToItem(listData().size - 1)
        }
    }

    Surface(
        modifier = modifier,
        color = backGroundColor,
        shape = containerShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp),
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                border = BorderStroke(1.dp, Color.White),
                color = Color(0xffffffff),
                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
            ) {
                Text(
                    text = title,
                    color = Color.Black,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
            }

            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize()
                ) {
                    itemsIndexed(
                        items = listData(),
                        key =  { index, _ -> index }
                    ) { _, log ->
                        Text(
                            text = log.toString(),
                            color = log.severity.color(),
                            fontFamily = FontFamily.Monospace,
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 1.5.em,
                            softWrap = false
                        )
                    }
                }

                VerticalScrollbar(
                    adapter = rememberScrollbarAdapter(listState),
                    modifier = Modifier
                        .clip(shapeContainer)
                        .align(Alignment.CenterEnd)
                        .background(Color.Black.copy(alpha = 0.4f))
                        .fillMaxHeight()
                        .width(8.dp),
                    style = defaultScrollbarStyle().copy(
                        unhoverColor = Color.White.copy(alpha = 0.4f),
                        hoverColor = Color.White.copy(alpha = 0.8f)
                    )
                )
            }
        }
    }
}
