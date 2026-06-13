package de.visualdigits.msfs2024tools.domain.util

import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.errorhandling.LogMessage
import de.visualdigits.common.domain.model.errorhandling.LogMessage.Companion.logMessage
import java.io.File

fun File.copyToIfNotExists(
    targetFile: File,
    logger: (LogMessage) -> Unit,
) {
    if (!targetFile.exists()) {
        logger(logMessage(Severity.Info, "Copy file '${this.canonicalPath}' to '$targetFile'..."))
        this.copyTo(targetFile)
    }
}

fun File.createDirectoryIfNotExists(
    logger: (LogMessage) -> Unit,
): File {
    if (!exists()) {
        logger(logMessage(Severity.Info, "Creating target directory '${this.canonicalPath}'"))
        if (!mkdirs()) {
            logger(logMessage(Severity.Error, "Could not create target directory"))
        }
    }

    return this
}
