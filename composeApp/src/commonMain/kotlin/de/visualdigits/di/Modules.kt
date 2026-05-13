package de.visualdigits.di

import de.de.visualdigits.data.database.mapper.DriverFactory
import de.visualdigits.msfs2024tools.SettingsDatabase
import de.visualdigits.msfs2024tools.SettingsDatabaseQueries
import de.visualdigits.msfs2024tools.data.datasource.ConfigurationDataSource
import de.visualdigits.msfs2024tools.data.datasource.FilesystemConfigurationDataSource
import de.visualdigits.msfs2024tools.data.repository.DefaultConfigurationRepository
import de.visualdigits.msfs2024tools.data.repository.DefaultMsfs2024Service
import de.visualdigits.msfs2024tools.domain.service.ConfigurationRepository
import de.visualdigits.msfs2024tools.domain.service.Msfs2024Service
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {

    singleOf(::Msfs2024ToolsViewModel)

    single {
        val driver = get<DriverFactory>().createDriver()
        SettingsDatabase(driver)
    }
    single<SettingsDatabaseQueries> {
        get<SettingsDatabase>().settingsDatabaseQueries
    }

    singleOf(::FilesystemConfigurationDataSource).bind<ConfigurationDataSource>()
    singleOf(::DefaultConfigurationRepository).bind<ConfigurationRepository>()
    singleOf(::DefaultMsfs2024Service).bind<Msfs2024Service>()
}
