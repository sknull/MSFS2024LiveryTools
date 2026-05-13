package de.de.visualdigits.data.database.mapper

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import de.visualdigits.msfs2024tools.SettingsDatabase
import java.io.File
import java.nio.file.Paths

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        val userHome = System.getProperty("user.home")
        val dbDirectory = Paths.get(userHome, ".msfs2024liverytools").toFile()
        if (!dbDirectory.exists()) {
            dbDirectory.mkdirs()
        }
        val dbPath = File(dbDirectory, "settings.db").canonicalPath
        val driver: SqlDriver = JdbcSqliteDriver(
            url = "jdbc:sqlite:$dbPath",
            schema = SettingsDatabase.Schema
        )
        driver.execute(null, "PRAGMA foreign_keys = ON;", 0)
        return driver
    }
}
