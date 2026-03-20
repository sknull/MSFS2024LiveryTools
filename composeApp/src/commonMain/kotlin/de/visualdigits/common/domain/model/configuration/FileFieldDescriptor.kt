package de.visualdigits.common.domain.model.configuration

import de.visualdigits.common.domain.model.FileMode
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import java.io.File

open class FileFieldDescriptor(
    key: String,

    label: StringResource,
    toolTip: StringResource? = null,

    visible: Boolean = true,
    readOnly: Boolean = false,

    val fileMode: FileMode,
    var startDirectory: () -> File = { File(System.getProperty("user.home")) },

    options: () -> List<Triple<String, StringResource?, DrawableResource?>> = { listOf() }
): AbstractFieldDescriptor<FileFieldDescriptor, File, File>(
    fieldClass = File::class,
    key = key,
    label = label,
    toolTip = toolTip,
    visible = visible,
    readOnly = readOnly,
    options = options
)
