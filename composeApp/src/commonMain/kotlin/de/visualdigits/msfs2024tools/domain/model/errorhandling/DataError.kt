package de.visualdigits.msfs2024tools.domain.model.errorhandling

import de.visualdigits.common.domain.model.Error

sealed interface DataError: Error {

    enum class Remote: DataError {
        REQUEST_TIMEOUT,
        NO_INTERNET,
        SERVER,
        SERIALIZATION,
        UNKNOWN
    }

    enum class Local: DataError {
        FILE_NOT_FOUND,
        DISK_FULL,
        SERIALIZATION,
        UNKNOWN_FIELD,
        UNKNOWN
    }
}
