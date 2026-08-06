package de.visualdigits.msfs2024tools.presentation.style

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.form.EditableListResources
import de.visualdigits.common.domain.model.form.FileChooserResources
import de.visualdigits.common.domain.model.form.FormFieldResources
import de.visualdigits.common.domain.model.form.FormResources
import de.visualdigits.common.domain.model.form.LocalEditableListResources
import de.visualdigits.common.domain.model.form.LocalFileChooserResources
import de.visualdigits.common.domain.model.form.LocalFormFieldResources
import de.visualdigits.common.domain.model.form.LocalFormResources
import de.visualdigits.common.domain.model.ui.UiText
import de.visualdigits.common.presentation.components.util.LocalSwitchColors
import de.visualdigits.common.presentation.components.util.switchBoxColors
import de.visualdigits.common.presentation.model.LocalPlatformScrollbarStyle
import de.visualdigits.common.presentation.model.PlatformScrollbarStyle
import de.visualdigits.compose.resources.Res
import de.visualdigits.compose.resources.icon_add_24px
import de.visualdigits.compose.resources.icon_cancel_24px
import de.visualdigits.compose.resources.icon_check_small_24px
import de.visualdigits.compose.resources.icon_delete_24px
import de.visualdigits.compose.resources.icon_edit_24px
import de.visualdigits.compose.resources.icon_folder_open_24px
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppCompositionProvider(
    content: @Composable () -> Unit
) {
    val platformScrollbarStyle = PlatformScrollbarStyle(
        minimalHeight = 16.dp,
        thickness = 8.dp,
        shape = RoundedCornerShape(4.dp),
        hoverDurationMillis = 300,
        unhoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
        hoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    )
    val formResource = FormResources(
        backgroundColor = Color.Gray,
        buttonShape = MaterialTheme.shapes.extraSmall,
        iconOk = painterResource(Res.drawable.icon_check_small_24px),
        iconCancel = painterResource(Res.drawable.icon_cancel_24px),
        buttonColor = Color.Black,
        containerShape = MaterialTheme.shapes.small,
    )
    val formFieldResources = FormFieldResources(
        fieldHeight = 50.dp,
        textStyle = MaterialTheme.typography.bodyMedium,
        iconTint = Color.White,
        shape = MaterialTheme.shapes.extraSmall,
        focusedBorderColor = MaterialTheme.colorScheme.outline,
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface
    )
    val fileChooserResources = FileChooserResources(
        iconFolder = painterResource(Res.drawable.icon_folder_open_24px),
        titleDirectories = "Choose Directory",
        titleFiles = "Choose File",
    )
    val editableListResources = EditableListResources(
        tooltipAdd = UiText.DynamicString("Add..."),
        titleAdd = UiText.DynamicString("Add"),
        iconAdd = Res.drawable.icon_add_24px,
        titleEdit = UiText.DynamicString("Edit"),
        iconEdit = Res.drawable.icon_edit_24px,
        toolTipDelete = UiText.DynamicString("Delete"),
        iconDelete = Res.drawable.icon_delete_24px,
        toolTipEdit = UiText.DynamicString("Edit"),
        labelOk = UiText.DynamicString("Ok"),
        iconOk = Res.drawable.icon_check_small_24px,
        labelCancel = UiText.DynamicString("Cancel"),
        iconCancel = Res.drawable.icon_cancel_24px
    )
    val switchColors = switchBoxColors()

    CompositionLocalProvider(
        LocalFormResources provides formResource,
        LocalFormFieldResources provides formFieldResources,
        LocalEditableListResources provides editableListResources,
        LocalPlatformScrollbarStyle provides platformScrollbarStyle,
        LocalFileChooserResources provides fileChooserResources,
        LocalSwitchColors provides switchColors
    ) {
        content()
    }
}
