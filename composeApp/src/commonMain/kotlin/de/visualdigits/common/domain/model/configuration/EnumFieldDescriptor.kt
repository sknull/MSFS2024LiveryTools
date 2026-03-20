package de.visualdigits.common.domain.model.configuration

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import kotlin.reflect.KClass

open class EnumFieldDescriptor<V : Any>(
    fieldClass: KClass<V>,

    key: String,

    label: StringResource,
    toolTip: StringResource? = null,

    visible: Boolean = true,
    readOnly: Boolean = false,

    options: () -> List<Triple<String, StringResource?, DrawableResource?>> = { listOf() },
    sorted: Boolean = false
): AbstractFieldDescriptor<EnumFieldDescriptor<V>, V, V>(
    fieldClass = fieldClass,
    key = key,
    label = label,
    toolTip = toolTip,
    visible = visible,
    readOnly = readOnly,
    options = options,
    sorted = sorted
)
