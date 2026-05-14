package de.visualdigits.msfs2024tools.domain.model.configuration

import de.visualdigits.common.domain.model.configuration.FieldKey

enum class SK : FieldKey<SK> {

    version,
    language,
    simType,
    sdkRoot,
    layoutGeneratorToolPath,
    nvidiaTextureToolPath,
    mainLibraryRootFolder,
    projectRootFolder,
    airplanes,
    spacer
    ;
}
