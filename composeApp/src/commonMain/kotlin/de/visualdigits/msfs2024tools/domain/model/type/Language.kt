package de.visualdigits.msfs2024tools.domain.model.type

import de.visualdigits.common.domain.model.StringResourceEnumerable
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.language_de
import msfs2024liverytools.composeapp.generated.resources.language_en
import msfs2024liverytools.composeapp.generated.resources.language_es
import msfs2024liverytools.composeapp.generated.resources.language_fr
import msfs2024liverytools.composeapp.generated.resources.language_it
import msfs2024liverytools.composeapp.generated.resources.language_tr
import org.jetbrains.compose.resources.StringResource
import java.util.Locale

enum class Language(
    override val resourceId: StringResource,
    val locale: Locale
) : StringResourceEnumerable {

    DE(Res.string.language_de, Locale.GERMANY),

    EN(Res.string.language_en, Locale.US),

    FR(Res.string.language_fr, Locale.FRANCE),

    ES(Res.string.language_es, Locale.Builder().setLanguage("es").setLanguageTag("ES").build()),

    TR(Res.string.language_tr, Locale.Builder().setLanguage("tr").setLanguageTag("TR").build()),

    IT(Res.string.language_it, Locale.ITALY),
}
