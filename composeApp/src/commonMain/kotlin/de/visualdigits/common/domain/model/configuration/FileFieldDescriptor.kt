package de.visualdigits.common.domain.model.configuration

import de.visualdigits.common.domain.model.FileMode
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import java.io.File

/**
 * Represents a field which should provide a file or directory picker.
 */
class FileFieldDescriptor<K : FieldKey<K>>(
    key: K,

    label: StringResource,
    toolTip: StringResource? = null,

    visible: Boolean = true,
    readOnly: Boolean = false,

    val fileMode: FileMode,
    var startDirectory: (AbstractConfiguration<*,*>?) -> File = {
        File(System.getProperty("user.home"))
    },

    options: () -> List<Triple<String, StringResource?, DrawableResource?>> = { listOf() },
): AbstractFieldDescriptor<File, File, K>(
    fieldClass = File::class,
    key = key,
    label = label,
    toolTip = toolTip,
    visible = visible,
    readOnly = readOnly,
    options = options,
    keyFactory = FileKeyFactory
)

class FileKeyFactory {

    companion object : KeyFactory<File> {

        override fun fromString(value: String?): File?  = value?.let { v -> File(v) }

        override fun stringValue(value: Any?): String? = (value as? File)?.canonicalPath
    }
}
