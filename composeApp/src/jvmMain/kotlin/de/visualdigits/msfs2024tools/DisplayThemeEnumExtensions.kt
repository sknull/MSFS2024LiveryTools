package de.visualdigits.msfs2024tools

import com.formdev.flatlaf.FlatDarculaLaf
import de.visualdigits.msfs2024tools.presentation.style.DisplayThemeEnum
import javax.swing.LookAndFeel

val DisplayThemeEnum.laf: LookAndFeel
    get() = when (this) {
        DisplayThemeEnum.DARK -> FlatDarculaLaf()
    }
