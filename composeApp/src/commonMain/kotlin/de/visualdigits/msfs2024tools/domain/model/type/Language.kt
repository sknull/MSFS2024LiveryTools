package de.visualdigits.msfs2024tools.domain.model.type

import de.visualdigits.common.domain.model.StringResourceEnumerable
import de.visualdigits.common.domain.model.configuration.KeyFactory
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.flag_bg
import msfs2024liverytools.composeapp.generated.resources.flag_cn
import msfs2024liverytools.composeapp.generated.resources.flag_cz
import msfs2024liverytools.composeapp.generated.resources.flag_de
import msfs2024liverytools.composeapp.generated.resources.flag_dk
import msfs2024liverytools.composeapp.generated.resources.flag_en
import msfs2024liverytools.composeapp.generated.resources.flag_es
import msfs2024liverytools.composeapp.generated.resources.flag_fi
import msfs2024liverytools.composeapp.generated.resources.flag_fr
import msfs2024liverytools.composeapp.generated.resources.flag_gr
import msfs2024liverytools.composeapp.generated.resources.flag_hr
import msfs2024liverytools.composeapp.generated.resources.flag_hu
import msfs2024liverytools.composeapp.generated.resources.flag_ir
import msfs2024liverytools.composeapp.generated.resources.flag_it
import msfs2024liverytools.composeapp.generated.resources.flag_jp
import msfs2024liverytools.composeapp.generated.resources.flag_nl
import msfs2024liverytools.composeapp.generated.resources.flag_no
import msfs2024liverytools.composeapp.generated.resources.flag_pl
import msfs2024liverytools.composeapp.generated.resources.flag_pt
import msfs2024liverytools.composeapp.generated.resources.flag_ro
import msfs2024liverytools.composeapp.generated.resources.flag_rs
import msfs2024liverytools.composeapp.generated.resources.flag_ru
import msfs2024liverytools.composeapp.generated.resources.flag_se
import msfs2024liverytools.composeapp.generated.resources.flag_sk
import msfs2024liverytools.composeapp.generated.resources.flag_sl
import msfs2024liverytools.composeapp.generated.resources.flag_tr
import msfs2024liverytools.composeapp.generated.resources.language_bg
import msfs2024liverytools.composeapp.generated.resources.language_cn
import msfs2024liverytools.composeapp.generated.resources.language_cz
import msfs2024liverytools.composeapp.generated.resources.language_de
import msfs2024liverytools.composeapp.generated.resources.language_dk
import msfs2024liverytools.composeapp.generated.resources.language_en
import msfs2024liverytools.composeapp.generated.resources.language_es
import msfs2024liverytools.composeapp.generated.resources.language_fi
import msfs2024liverytools.composeapp.generated.resources.language_fr
import msfs2024liverytools.composeapp.generated.resources.language_gr
import msfs2024liverytools.composeapp.generated.resources.language_hr
import msfs2024liverytools.composeapp.generated.resources.language_hu
import msfs2024liverytools.composeapp.generated.resources.language_ir
import msfs2024liverytools.composeapp.generated.resources.language_it
import msfs2024liverytools.composeapp.generated.resources.language_jp
import msfs2024liverytools.composeapp.generated.resources.language_nl
import msfs2024liverytools.composeapp.generated.resources.language_no
import msfs2024liverytools.composeapp.generated.resources.language_pl
import msfs2024liverytools.composeapp.generated.resources.language_pt
import msfs2024liverytools.composeapp.generated.resources.language_ro
import msfs2024liverytools.composeapp.generated.resources.language_rs
import msfs2024liverytools.composeapp.generated.resources.language_ru
import msfs2024liverytools.composeapp.generated.resources.language_se
import msfs2024liverytools.composeapp.generated.resources.language_sk
import msfs2024liverytools.composeapp.generated.resources.language_sl
import msfs2024liverytools.composeapp.generated.resources.language_tr
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import java.util.Locale

enum class Language(
    override val stringResourceId: StringResource,
    override val drawableResourceId: DrawableResource,
    val locale: Locale
) : StringResourceEnumerable<Language> {

    BG(Res.string.language_bg, Res.drawable.flag_bg, Locale.Builder().setLanguage("bg").setLanguageTag("BG").build()),
    CZ(Res.string.language_cz, Res.drawable.flag_cz, Locale.Builder().setLanguage("cz").setLanguageTag("CZ").build()),
    CN(Res.string.language_cn, Res.drawable.flag_cn, Locale.Builder().setLanguage("cn").setLanguageTag("CN").build()),
    DE(Res.string.language_de, Res.drawable.flag_de, Locale.GERMANY),
    DK(Res.string.language_dk, Res.drawable.flag_dk, Locale.Builder().setLanguage("dk").setLanguageTag("DK").build()),
    EN(Res.string.language_en, Res.drawable.flag_en, Locale.US),
    ES(Res.string.language_es, Res.drawable.flag_es, Locale.Builder().setLanguage("es").setLanguageTag("ES").build()),
    FI(Res.string.language_fi, Res.drawable.flag_fi, Locale.Builder().setLanguage("fi").setLanguageTag("FI").build()),
    FR(Res.string.language_fr, Res.drawable.flag_fr, Locale.FRANCE),
    GR(Res.string.language_gr, Res.drawable.flag_gr, Locale.Builder().setLanguage("gr").setLanguageTag("GR").build()),
    HR(Res.string.language_hr, Res.drawable.flag_hr, Locale.Builder().setLanguage("hr").setLanguageTag("HR").build()),
    HU(Res.string.language_hu, Res.drawable.flag_hu, Locale.Builder().setLanguage("hu").setLanguageTag("HU").build()),
    IR(Res.string.language_ir, Res.drawable.flag_ir, Locale.Builder().setLanguage("ir").setLanguageTag("IR").build()),
    IT(Res.string.language_it, Res.drawable.flag_it, Locale.ITALY),
    JP(Res.string.language_jp, Res.drawable.flag_jp, Locale.Builder().setLanguage("jp").setLanguageTag("JP").build()),
    NL(Res.string.language_nl, Res.drawable.flag_nl, Locale.Builder().setLanguage("nl").setLanguageTag("NL").build()),
    NO(Res.string.language_no, Res.drawable.flag_no, Locale.Builder().setLanguage("no").setLanguageTag("NO").build()),
    PL(Res.string.language_pl, Res.drawable.flag_pl, Locale.Builder().setLanguage("pl").setLanguageTag("PL").build()),
    PT(Res.string.language_pt, Res.drawable.flag_pt, Locale.Builder().setLanguage("pt").setLanguageTag("PT").build()),
    RO(Res.string.language_ro, Res.drawable.flag_ro, Locale.Builder().setLanguage("ro").setLanguageTag("RO").build()),
    RS(Res.string.language_rs, Res.drawable.flag_rs, Locale.Builder().setLanguage("rs").setLanguageTag("RS").build()),
    RU(Res.string.language_ru, Res.drawable.flag_ru, Locale.Builder().setLanguage("ru").setLanguageTag("RU").build()),
    SE(Res.string.language_se, Res.drawable.flag_se, Locale.Builder().setLanguage("se").setLanguageTag("SE").build()),
    SK(Res.string.language_sk, Res.drawable.flag_sk, Locale.Builder().setLanguage("sk").setLanguageTag("SK").build()),
    SL(Res.string.language_sl, Res.drawable.flag_sl, Locale.Builder().setLanguage("sl").setLanguageTag("SL").build()),
    TR(Res.string.language_tr, Res.drawable.flag_tr, Locale.Builder().setLanguage("tr").setLanguageTag("TR").build()),
    ;

    companion object : KeyFactory<Language> {

        override fun fromString(value: String?): Language? {
            return entries.find { e -> e.name == value }
        }

        override fun stringValue(value: Any?): String? = (value as? Language)?.name
    }
}
