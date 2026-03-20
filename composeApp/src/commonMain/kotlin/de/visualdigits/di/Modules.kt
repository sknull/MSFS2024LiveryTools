package de.visualdigits.di

import de.visualdigits.msfs2024tools.data.datasource.ConfigurationDataSource
import de.visualdigits.msfs2024tools.data.datasource.FilesystemConfigurationDataSource
import de.visualdigits.msfs2024tools.data.repository.DefaultConfigurationRepository
import de.visualdigits.msfs2024tools.data.repository.DefaultMsfs2024Service
import de.visualdigits.msfs2024tools.domain.service.ConfigurationRepository
import de.visualdigits.msfs2024tools.domain.service.Msfs2024Service
import de.visualdigits.msfs2024tools.presentation.model.Msfs2024ToolsViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedModule = module {

    singleOf(::FilesystemConfigurationDataSource).bind<ConfigurationDataSource>()

    singleOf(::DefaultConfigurationRepository).bind<ConfigurationRepository>()

    singleOf(::DefaultMsfs2024Service).bind<Msfs2024Service>()

    viewModelOf(::Msfs2024ToolsViewModel)
}
