package de.visualdigits.common.domain.service

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.platformLogWriter
import java.io.File

actual fun getPlatformLogWriters(): List<LogWriter> =
    listOf(JvmFileLogWriter(File("Msfs2024LiveryTools.log")), platformLogWriter())
