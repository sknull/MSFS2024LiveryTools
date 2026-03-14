package de.visualdigits.msfs2024tools.presentation.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import co.touchlab.kermit.Logger
import de.visualdigits.msfs2024tools.domain.model.configuration.GlobalConfiguration
import de.visualdigits.msfs2024tools.domain.model.type.Language
import de.visualdigits.msfs2024tools.domain.service.ConfigurationRepository
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.icon_info_24px
import msfs2024liverytools.composeapp.generated.resources.icon_language_24px
import msfs2024liverytools.composeapp.generated.resources.menu_about
import msfs2024liverytools.composeapp.generated.resources.menu_help
import msfs2024liverytools.composeapp.generated.resources.menu_language
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import java.util.Locale


@Composable
fun FrameWindowScope.Msfs2024MenuBar(
    scope: CoroutineScope,
    showInfoDialog: (Boolean) -> Unit,
    snackbarHostState: SnackbarHostState,
    languageTrigger: (Locale) -> Unit
) {

    val msfs2024ToolsViewModel = koinViewModel<Msfs2024ToolsViewModel>()

    MenuBar {
        Menu(
            text = stringResource(Res.string.menu_help),
        ) {
            Item(
                text = stringResource(Res.string.menu_about),
                icon = painterResource(Res.drawable.icon_info_24px),
                onClick = {
                    showInfoDialog(true)
                }
            )
        }

        Menu(
            text = stringResource(Res.string.menu_language),
        ) {
            Item(
                text = stringResource(Language.DE.resourceId),
                icon = painterResource(Res.drawable.icon_language_24px),
                onClick = {
                    updateLanguage(
                        scope,
                        Language.DE,
                        msfs2024ToolsViewModel.state.value.globalConfiguration?:error("No global configuration loaded"),
                        msfs2024ToolsViewModel.configurationRepository,
                        snackbarHostState
                    ) {
                        languageTrigger(it)
                    }
                }
            )

            Item(
                text = stringResource(Language.EN.resourceId),
                icon = painterResource(Res.drawable.icon_language_24px),
                onClick = {
                    updateLanguage(
                        scope,
                        Language.EN,
                        msfs2024ToolsViewModel.state.value.globalConfiguration?:error("No global configuration loaded"),
                        msfs2024ToolsViewModel.configurationRepository,
                        snackbarHostState
                    ) {
                        languageTrigger(it)
                    }
                }
            )
        }
    }
}

private fun updateLanguage(
    scope: CoroutineScope,
    language: Language,
    globalConfiguration: GlobalConfiguration,
    configurationRepository: ConfigurationRepository,
    snackbarHostState: SnackbarHostState,
    onSuccess: (Locale) -> Unit
) {
    scope.launch {
        try {
            Locale.setDefault(language.locale)
            globalConfiguration.set("language", language.name)
            withContext((Dispatchers.IO)) {
                configurationRepository.saveGlobalConfiguration(globalConfiguration)
            }
            onSuccess(language.locale)
        } catch (e: Exception) {
            snackbarHostState.showSnackbar(
                message = "Fehler beim Speichern der Konfiguration: ${e.localizedMessage}",
                duration = SnackbarDuration.Long,
                withDismissAction = true
            )
            Logger.e("ERROR: Save failed with message '${e.message}'", e)
        }
    }
}
