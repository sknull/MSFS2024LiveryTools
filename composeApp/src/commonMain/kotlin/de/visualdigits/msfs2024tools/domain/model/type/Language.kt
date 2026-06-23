package de.visualdigits.msfs2024tools.domain.model.type

import de.visualdigits.common.domain.model.configuration.keyfactory.KeyFactory
import de.visualdigits.common.domain.model.ui.StringResourceEnumerable
import de.visualdigits.common.domain.model.ui.UiText
import de.visualdigits.compose.resources.Res
import de.visualdigits.compose.resources.flag_bg
import de.visualdigits.compose.resources.flag_cn
import de.visualdigits.compose.resources.flag_cz
import de.visualdigits.compose.resources.flag_de
import de.visualdigits.compose.resources.flag_dk
import de.visualdigits.compose.resources.flag_en
import de.visualdigits.compose.resources.flag_es
import de.visualdigits.compose.resources.flag_fi
import de.visualdigits.compose.resources.flag_fr
import de.visualdigits.compose.resources.flag_gr
import de.visualdigits.compose.resources.flag_hr
import de.visualdigits.compose.resources.flag_hu
import de.visualdigits.compose.resources.flag_ir
import de.visualdigits.compose.resources.flag_it
import de.visualdigits.compose.resources.flag_jp
import de.visualdigits.compose.resources.flag_nl
import de.visualdigits.compose.resources.flag_no
import de.visualdigits.compose.resources.flag_pl
import de.visualdigits.compose.resources.flag_pt
import de.visualdigits.compose.resources.flag_ro
import de.visualdigits.compose.resources.flag_rs
import de.visualdigits.compose.resources.flag_ru
import de.visualdigits.compose.resources.flag_se
import de.visualdigits.compose.resources.flag_sk
import de.visualdigits.compose.resources.flag_sl
import de.visualdigits.compose.resources.flag_tr
import de.visualdigits.compose.resources.language_bg
import de.visualdigits.compose.resources.language_cs
import de.visualdigits.compose.resources.language_de
import de.visualdigits.compose.resources.language_dk
import de.visualdigits.compose.resources.language_en
import de.visualdigits.compose.resources.language_es
import de.visualdigits.compose.resources.language_fi
import de.visualdigits.compose.resources.language_fr
import de.visualdigits.compose.resources.language_gr
import de.visualdigits.compose.resources.language_hr
import de.visualdigits.compose.resources.language_hu
import de.visualdigits.compose.resources.language_ir
import de.visualdigits.compose.resources.language_it
import de.visualdigits.compose.resources.language_jp
import de.visualdigits.compose.resources.language_nl
import de.visualdigits.compose.resources.language_no
import de.visualdigits.compose.resources.language_pl
import de.visualdigits.compose.resources.language_pt
import de.visualdigits.compose.resources.language_ro
import de.visualdigits.compose.resources.language_rs
import de.visualdigits.compose.resources.language_ru
import de.visualdigits.compose.resources.language_se
import de.visualdigits.compose.resources.language_sk
import de.visualdigits.compose.resources.language_sl
import de.visualdigits.compose.resources.language_tr
import de.visualdigits.compose.resources.language_zh
import org.jetbrains.compose.resources.DrawableResource
import java.util.Locale

enum class Language(
    override val uiText: UiText,
    override val drawableResourceId: DrawableResource?,
    val localeCode: String
) : StringResourceEnumerable<Language> {

    BG(UiText.StringResourceId(Res.string.language_bg), Res.drawable.flag_bg, "bg"),
    CS(UiText.StringResourceId(Res.string.language_cs), Res.drawable.flag_cz, "cs"),
    DE(UiText.StringResourceId(Res.string.language_de), Res.drawable.flag_de, "de"),
    DK(UiText.StringResourceId(Res.string.language_dk), Res.drawable.flag_dk, "dk"),
    EN(UiText.StringResourceId(Res.string.language_en), Res.drawable.flag_en, "en"),
    ES(UiText.StringResourceId(Res.string.language_es), Res.drawable.flag_es, "es"),
    FI(UiText.StringResourceId(Res.string.language_fi), Res.drawable.flag_fi, "fi"),
    FR(UiText.StringResourceId(Res.string.language_fr), Res.drawable.flag_fr, "fr"),
    GR(UiText.StringResourceId(Res.string.language_gr), Res.drawable.flag_gr, "gr"),
    HR(UiText.StringResourceId(Res.string.language_hr), Res.drawable.flag_hr, "hr"),
    HU(UiText.StringResourceId(Res.string.language_hu), Res.drawable.flag_hu, "hu"),
    IR(UiText.StringResourceId(Res.string.language_ir), Res.drawable.flag_ir, "ir"),
    IT(UiText.StringResourceId(Res.string.language_it), Res.drawable.flag_it, "it"),
    JA(UiText.StringResourceId(Res.string.language_jp), Res.drawable.flag_jp, "ja"),
    NL(UiText.StringResourceId(Res.string.language_nl), Res.drawable.flag_nl, "nl"),
    NO(UiText.StringResourceId(Res.string.language_no), Res.drawable.flag_no, "no"),
    PL(UiText.StringResourceId(Res.string.language_pl), Res.drawable.flag_pl, "pl"),
    PT(UiText.StringResourceId(Res.string.language_pt), Res.drawable.flag_pt, "pt"),
    RO(UiText.StringResourceId(Res.string.language_ro), Res.drawable.flag_ro, "ro"),
    RS(UiText.StringResourceId(Res.string.language_rs), Res.drawable.flag_rs, "rs"),
    RU(UiText.StringResourceId(Res.string.language_ru), Res.drawable.flag_ru, "ru"),
    SE(UiText.StringResourceId(Res.string.language_se), Res.drawable.flag_se, "se"),
    SK(UiText.StringResourceId(Res.string.language_sk), Res.drawable.flag_sk, "sk"),
    SL(UiText.StringResourceId(Res.string.language_sl), Res.drawable.flag_sl, "sl"),
    TR(UiText.StringResourceId(Res.string.language_tr), Res.drawable.flag_tr, "tr"),
    ZH(UiText.StringResourceId(Res.string.language_zh), Res.drawable.flag_cn, "zh"),
    ;

    companion object : KeyFactory<Language> {

        override val options: List<Triple<Language, UiText?, DrawableResource?>> = Language.entries.map { e -> Triple(e, e.uiText, e.drawableResourceId) }

        override fun fromString(value: String?): Language? {
            return entries.find { e -> e.name == value }
        }

        override fun fromValue(value: Any?): Language? {
            return when (value) {
                is String -> Language.Companion.fromString(value)
                is Language -> value
                is Locale -> Language.entries.find { e -> e.localeCode == value }
                else -> null
            }
        }

        override fun stringValue(value: Any?): String? = (value as? Language)?.name
    }
}
