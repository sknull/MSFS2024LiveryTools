package de.visualdigits.msfs2024tools.presentation.screen.page

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.configuration.AbstractConfiguration
import de.visualdigits.common.domain.model.configuration.AbstractFieldDescriptor
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.domain.model.form.EditableListResources
import de.visualdigits.common.domain.model.ui.UiText
import de.visualdigits.common.presentation.components.form.EditableList
import de.visualdigits.compose.resources.Res
import de.visualdigits.compose.resources.add
import de.visualdigits.compose.resources.add_hint
import de.visualdigits.compose.resources.cancel
import de.visualdigits.compose.resources.delete
import de.visualdigits.compose.resources.edit
import de.visualdigits.compose.resources.icon_add_24px
import de.visualdigits.compose.resources.icon_cancel_24px
import de.visualdigits.compose.resources.icon_check_small_24px
import de.visualdigits.compose.resources.icon_delete_24px
import de.visualdigits.compose.resources.icon_edit_24px
import de.visualdigits.compose.resources.icon_folder_open_24px
import de.visualdigits.compose.resources.ok
import de.visualdigits.msfs2024tools.domain.model.configuration.PK
import de.visualdigits.msfs2024tools.domain.model.configuration.SK
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsAction
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsState
import de.visualdigits.msfs2024tools.presentation.style.gap
import org.jetbrains.compose.resources.painterResource

@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>> AirplanesTab(
    state: Msfs2024ToolsState,
    onProjectListAction: (Msfs2024ToolsAction) -> Unit
) {
    val fieldDescriptor = state.settings?.lookupFieldDescriptors[SK.airplanes]!! as AbstractFieldDescriptor<Any, Any, K, FK, Any>
    val currentOption = fieldDescriptor.currentOption(state.settings as AbstractConfiguration<*, K>)
    val currentValue = state.settings.getUnsafe(fieldDescriptor.key)
    EditableList(
        fieldState = FieldState(
            configuration = state.settings,
            fieldDescriptor = fieldDescriptor,
            options = fieldDescriptor.options(state.settings, null),
            currentValue = currentValue,
            currentOption = currentOption,
            currentOptionUIText = currentOption?.second ?: UiText.DynamicString(
                currentOption?.first?.toString() ?: ""
            ),
            valid = fieldDescriptor.valid(state.settings, currentValue)
        ),
        iconFolder = painterResource(Res.drawable.icon_folder_open_24px),
        resources = EditableListResources(
            titleAdd = UiText.StringResourceId(Res.string.add),
            titleEdit = UiText.StringResourceId(Res.string.edit),
            tooltipAdd = UiText.StringResourceId(Res.string.add_hint),
            iconAdd = Res.drawable.icon_add_24px,
            toolTipEdit = UiText.StringResourceId(Res.string.edit),
            iconEdit = Res.drawable.icon_edit_24px,
            toolTipDelete = UiText.StringResourceId(Res.string.delete),
            iconDelete = Res.drawable.icon_delete_24px,
            labelOk = UiText.StringResourceId(Res.string.ok),
            iconOk = Res.drawable.icon_check_small_24px,
            labelCancel = UiText.StringResourceId(Res.string.cancel),
            iconCancel = Res.drawable.icon_cancel_24px
        ),
        fieldHeight = 70.dp,
        space = MaterialTheme.shapes.gap,
        focusedBorderColor = MaterialTheme.colorScheme.outline,
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
        iconTint = MaterialTheme.colorScheme.onBackground,
        buttonShape = MaterialTheme.shapes.extraSmall,
        containerShape = MaterialTheme.shapes.small,
        buttonColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        scrollable = true,
        textStyle = MaterialTheme.typography.bodyMedium,
        onValueChange = { keyValue ->
            if (state.settings.get<List<String>>(SK.airplanes) != (keyValue.value as? String)?.split(",")) {
                val projectConfigurations = state.projectConfigurations
                val newConfigurations = projectConfigurations.filter { p ->
                    (p.get<List<String>>(PK.airplaneName) ?: "") == (keyValue.previousValue ?: "")
                }.map { p ->                   // prevent to rename unfinished edits
                    if (p.get<List<String>>(PK.airplaneName) != null && keyValue.newValue != null) {
                        p.copy(PK.airplaneName, keyValue.newValue)
                    } else {
                        p
                    }
                }
                onProjectListAction(
                    Msfs2024ToolsAction.OnSaveAirplanesClick(
                        settings = state.settings.copy(key = SK.airplanes, value = keyValue.value),
                        projectConfigurations = newConfigurations
                    )
                )
            }
        }
    ) { descriptor, value ->
        when (descriptor?.key) {
            SK.airplanes -> state.projectConfigurations.none { p ->
                val airplaneName = p.get<String>(PK.airplaneName) ?: ""
                airplaneName == value
            }

            else -> true
        }
    }
}
