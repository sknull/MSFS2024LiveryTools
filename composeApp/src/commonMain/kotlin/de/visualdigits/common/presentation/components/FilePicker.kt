package de.visualdigits.common.presentation.components

import androidx.compose.runtime.Composable
import de.visualdigits.common.domain.model.FileMode
import deskit.dialogs.defaults.FileChooserDefaults
import deskit.dialogs.defaults.FolderChooserDefaults
import deskit.dialogs.file.filechooser.FileChooserDialog
import deskit.dialogs.file.folderchooser.FolderChooserDialog
import msfs2024liverytools.composeapp.generated.resources.Res
import msfs2024liverytools.composeapp.generated.resources.choose_file
import org.jetbrains.compose.resources.stringResource
import java.io.File


@Composable
fun FilePicker(
    fileMode: FileMode,
    startDirectory: File = File(System.getProperty("user.home")),
    allowedExtensions: List<String>,
    onFileSelected: (File) -> Unit,
    onCancel: () -> Unit,
) {
    when (fileMode) {
        FileMode.FILES_ONLY -> {
            FileChooserDialog(
                title = stringResource((Res.string.choose_file)),
                startDirectory = startDirectory,
                allowedExtensions = allowedExtensions,
                resizableFileInfoDialog = true,
                allowSoftWrapFileName = false,
                colors = FileChooserDefaults.colors(),
                onFileSelected = { file ->
                    onFileSelected(file)
                },
                onCancel = {
                    onCancel()
                }
            )
        }

        FileMode.DIRECTORIES_ONLY -> {
            FolderChooserDialog(
                title = stringResource((Res.string.choose_file)),
                allowSoftWrapFileName = false,
                colors = FolderChooserDefaults.colors(),
                onFolderSelected = { folder ->
                    onFileSelected(folder)
                },
                onCancel = {
                    onCancel()
                }
            )
        }

    }
}
