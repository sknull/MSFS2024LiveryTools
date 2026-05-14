package de.visualdigits.msfs2024tools.di

import de.visualdigits.msfs2024tools.data.database.DriverFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

actual val homeDirectory: String
    get() = File(System.getProperty("user.home"), ".msfs2024liverytools").canonicalPath

actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { OkHttp.create() }
        single<DriverFactory> { DriverFactory() }
    }
