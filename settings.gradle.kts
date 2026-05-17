rootProject.name = "MSFS2024LiveryTools"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")


pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://sonatype.com")
        maven("https://jitpack.io")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":composeApp")

includeBuild("../Stephans-KMP-Components") {
    dependencySubstitution {
        substitute(module("de.visualdigits.kmp:stephans-kmp-components"))
            .using(project(":library"))
    }
}
