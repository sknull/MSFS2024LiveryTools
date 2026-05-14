package de.visualdigits.msfs2024tools.di

import de.visualdigits.msfs2024tools.SettingsDatabase
import de.visualdigits.msfs2024tools.SettingsDatabaseQueries
import de.visualdigits.msfs2024tools.SettingsEntity
import de.visualdigits.msfs2024tools.data.database.DriverFactory
import de.visualdigits.msfs2024tools.data.datasource.DatabaseSettingsDataSource
import de.visualdigits.msfs2024tools.data.datasource.FilesystemSettingsDataSource
import de.visualdigits.msfs2024tools.data.mapper.projectsAdapter
import de.visualdigits.msfs2024tools.data.mapper.stringListAdapter
import de.visualdigits.msfs2024tools.data.repository.DefaultMsfs2024Service
import de.visualdigits.msfs2024tools.data.repository.DefaultSettingsRepository
import de.visualdigits.msfs2024tools.domain.service.Msfs2024Service
import de.visualdigits.msfs2024tools.domain.service.SettingsRepository
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

expect val homeDirectory: String

val sharedModule = module {

    single(named("homeDirectory")) { homeDirectory }

    singleOf(::Msfs2024ToolsViewModel)

    single {
        val driver = get<DriverFactory>().createDriver()
        SettingsDatabase(driver,
            SettingsEntityAdapter = SettingsEntity.Adapter(
                airplanesAdapter = stringListAdapter,
                projectsAdapter = projectsAdapter
            )
        )
    }
    single<SettingsDatabaseQueries> {
        get<SettingsDatabase>().settingsDatabaseQueries
    }

    singleOf(::FilesystemSettingsDataSource)
    singleOf(::DatabaseSettingsDataSource)

    singleOf(::DefaultSettingsRepository).bind<SettingsRepository>()
    singleOf(::DefaultMsfs2024Service).bind<Msfs2024Service>()
}
