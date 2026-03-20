package de.visualdigits.msfs2024tools.domain.model.configuration

import de.visualdigits.common.domain.model.configuration.FieldKey

enum class PK : FieldKey<PK> {

    airplaneName,
    liveryName,
    packageDir,
    packageTextureDir,
    modelTexturesDir,
    textureFormatPackage,
    textureFormatModel,
    textureTypes,
    spacer
    ;
}
