package de.visualdigits.common.domain.model

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

interface StringResourceEnumerable<T : StringResourceEnumerable<T>> : Enumerable<T> {

    val stringResourceId: StringResource
    val drawableResourceId: DrawableResource
}
