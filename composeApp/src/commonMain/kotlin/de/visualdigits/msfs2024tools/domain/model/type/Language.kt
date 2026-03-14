package de.visualdigits.msfs2024tools.domain.model.type

import de.visualdigits.common.domain.model.StringResourceEnumerable
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.language_de
import msfs2024liverytools.composeapp.generated.resources.language_en
import org.jetbrains.compose.resources.StringResource
import java.util.Locale

enum class Language(
    override val resourceId: StringResource,
    val locale: Locale
) : StringResourceEnumerable {

    DE(Res.string.language_de, Locale.GERMANY),

    EN(Res.string.language_en, Locale.US)
}
