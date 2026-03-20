package de.visualdigits.common.domain.model.configuration

import org.jetbrains.compose.resources.StringResource

open class StringFieldDescriptor(
    key: String,

    label: StringResource,
    toolTip: StringResource? = null,

    visible: Boolean = true,
    readOnly: Boolean = false,
): AbstractFieldDescriptor<StringFieldDescriptor, String, String>(
    fieldClass = String::class,
    key = key,
    label = label,
    toolTip = toolTip,
    visible = visible,
    readOnly = readOnly,
)
