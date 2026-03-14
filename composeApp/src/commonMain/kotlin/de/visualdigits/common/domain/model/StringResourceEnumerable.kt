package de.visualdigits.common.domain.model

import org.jetbrains.compose.resources.StringResource

interface StringResourceEnumerable : Enumerable {

    val resourceId: StringResource
}
