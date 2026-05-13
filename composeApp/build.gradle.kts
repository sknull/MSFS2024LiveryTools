import de.visualdigits.translation.util.TranslationUtil
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.gradle.pdf)
    alias(libs.plugins.sqlDelight)
    id("com.google.devtools.ksp") version "2.3.6"
    `maven-publish`
}


val version = "1.0.0-SNAPSHOT"
val buildNumber = System.getenv("GITHUB_RUN_NUMBER") ?: "9999"
val installerVersion = if (buildNumber == "9999") {
    version
} else {
    "$version.$buildNumber"
}

buildscript {
    dependencies {
        classpath(libs.proguardGradle)
    }
}

abstract class GenerateVersionTask : DefaultTask() {
    @get:Input
    abstract val appVersion: Property<String>

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun generate() {
        val outputFile = outputDirectory.file("AppVersion.kt").get().asFile
        outputFile.parentFile.mkdirs()
        outputFile.writeText("""package de.visualdigits.generated

data class AppVersion(
    val version: String = "${appVersion.get()}",
) : Comparable<AppVersion> {

    val numericParts: List<Int> = version
        .substringBefore("-")
        .split(".")
        .map { v -> v.toInt() }

    override fun compareTo(other: AppVersion): Int {
        var c = numericParts[0].compareTo(other.numericParts[0])
        var index = 1
        while (c == 0 && index < 3) {
            c = numericParts[index].compareTo(other.numericParts[index])
            index++
        }
        if (c == 0 && version.contains("-")) {
            c = -1
        }
        
        return c
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AppVersion

        return numericParts == other.numericParts
    }

    override fun hashCode(): Int {
        return numericParts.hashCode()
    }
}""")
    }
}

val generateVersionClass = tasks.register<GenerateVersionTask>("generateVersionClass") {
    group = "other"
    description = "Generates a version class to be used within the code"
    notCompatibleWithConfigurationCache("No caching supported.")
    appVersion.set(installerVersion)
    outputDirectory.set(layout.buildDirectory.dir("generated/version"))
}

kotlin {
    jvm()
    jvmToolchain(21)

    sqldelight {
        databases {
            create("SettingsDatabase") {
                packageName = "de.visualdigits.msfs2024tools"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            kotlin.srcDir(generateVersionClass)
        }

        commonMain.dependencies {
            implementation(compose.components.resources)

            implementation(libs.bundles.compose)
            implementation(libs.bundles.coil)
            implementation(libs.bundles.ktor)
            implementation(libs.bundles.koin)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation(libs.jetbrains.compose.navigation)

            implementation(libs.kotlin.xml.util)
            implementation(libs.kotlin.xml.serialization)
            implementation(libs.kotlinx.coroutines)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.io.core)

            implementation(libs.kermit)
            implementation(libs.deskit)

            implementation(libs.html.converter)

            implementation(libs.sqldelight.coroutines)
            implementation(libs.sqlite.bundled)

            implementation(libs.compose.colorpicker)
            implementation(libs.stephans.kmp.components)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.junit.jupiter.api)
            implementation(libs.junit.jupiter.engine)
            implementation(libs.junit.platform.launcher)
            implementation(libs.koin.test)
        }

        jvmMain.dependencies {
            implementation(libs.flatlaf)
            implementation(compose.desktop.currentOs)
            implementation(libs.skiko.awt.runtime.windows.x64)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.sqldelight.jvm)
            implementation(libs.kotlinx.io.core.jvm)
        }

        jvmTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.junit.jupiter.api)
            implementation(libs.junit.jupiter.engine)
            implementation(libs.junit.platform.launcher)
            implementation(libs.koin.test)
        }
    }
}

configurations.all {
    exclude(group = "ch.qos.logback", module = "logback-classic")
    exclude(group = "ch.qos.logback", module = "logback-core")
}

base {
    archivesName.set("msfs2024tools")
}

configurations.all {
    exclude(group = "org.jetbrains.compose.material", module = "material-desktop")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Tar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Zip> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

compose.desktop {
    application {
        mainClass = "de.visualdigits.msfs2024tools.MainKt"

        nativeDistributions {
            packageName = "de.visualdigits.msfs2024tools"
            packageVersion = "1.0.$buildNumber"
            includeAllModules = false
            modules(
                "java.instrument",
                "jdk.unsupported",
                "java.desktop",
                "java.xml",
                "java.naming",
                "java.prefs",
                "java.sql",
                "java.net.http"
            )
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Rpm)
            windows {
                upgradeUuid = "5cd976a1-608e-4891-9dc5-20fee6a8c579" // generated by randomness
                shortcut = true
                menu = true
                iconFile.set(project.file("src/commonMain/composeResources/drawable/favicon.ico"))
            }
            linux {
                // Diese Felder sind für .deb oft Pflicht!
                packageName = "news-home-reader"
                debMaintainer = "stephan@visualdigits.de"
                appCategory = "News"
                menuGroup = "Network"
                shortcut = true // Erzeugt den Startmenü-Eintrag
            }
            buildTypes {
                release {
                    proguard {
                        isEnabled.set(false)
                        optimize.set(false)
                    }
                }
            }
        }
    }
}

configurations.all {
    resolutionStrategy {
        val versionIo = libs.versions.version.kotlinx.io.core.get()
        force("org.jetbrains.kotlinx:kotlinx-io-core:$versionIo")
        force("org.jetbrains.kotlinx:kotlinx-io-bytestring:$versionIo")
    }
}

tasks.register("showDependencies") {
    group = "other"
    description = "Shows the list of dependencies"
    doLast {
        configurations.kotlinCompilerClasspath.get()
            .forEach { println("#### ${it.canonicalPath}") }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "de.visualdigits.compose.resources"
}

tasks.asciidoctorPdf {
    notCompatibleWithConfigurationCache("No caching supported.")
    baseDirFollowsSourceFile()
    setSourceDir(rootDir)
    sources {
        include("README.adoc")
    }
    setOutputDir(file(layout.buildDirectory.dir("asciidoc")))
    asciidoctorj {
        attributes(
            mapOf(
                "imagesdir" to rootDir.absolutePath,
                "source-highlighter" to "rouge",
                "icons" to "font"
            )
        )
    }
}

val copyPdfToDistribution = tasks.register<Copy>("copyPdfToDistribution",) {
    group = "documentation"
    description = "Copies the asciidoc pdf into the distribution"

    val pdfTask = tasks.asciidoctorPdf.get()
    dependsOn(tasks.asciidoctorPdf)
    from(pdfTask.outputDir)
    into(layout.buildDirectory.dir("compose/binaries/main/app/de.visualdigits.msfs2024tools"))
    include("**/*.pdf")
    eachFile { path = name }
}

val copyPdfToDocs = tasks.register<Copy>("copyPdfToDocs") {
    group = "documentation"
    description = "Copies the asciidoc pdf into the docs directory"

    val pdfTask = tasks.asciidoctorPdf.get()
    dependsOn(tasks.asciidoctorPdf)
    from(pdfTask.outputDir)
    into(file("$rootDir/docs"))
    include("**/*.pdf")
    eachFile { path = name }
}

tasks.matching { it.name == "createDistributable" }.all {
    finalizedBy(copyPdfToDistribution, copyPdfToDocs)
}

tasks.register<Zip>("zip") {
    group = "compose desktop"
    description = "Writes the artifact created by createDistributable to a zip file"

    dependsOn("createDistributable", copyPdfToDistribution, copyPdfToDocs)

    from(layout.buildDirectory.dir("compose/binaries/main/app"))
    from(tasks.asciidoctorPdf.map { it.outputDir }) {
        include("README.pdf") // oder "**/*.pdf"
        into("de.visualdigits.msfs2024tools")
    }

    archiveFileName.set("msfs2024tools_${project.version}.zip")
    destinationDirectory.set(layout.buildDirectory.dir("distributions"))
}

tasks.register("extractTranslations") {
    group = "localization"
    description = "Converts the string resources to a csv file under projectroot/translation/stringresources.csv."

    val projectRootDir = project.rootDir
    doLast {
        TranslationUtil.extractTranslation(projectRootDir)
    }
}

tasks.register("updateTranslations") {
    group = "localization"
    description = "Converts the translation csv back to string resources."

    val projectRootDir = project.rootDir
    doLast {
        TranslationUtil.updateTranslation(projectRootDir)
    }
}

tasks.register("joinUpdateTranslations") {
    group = "localization"
    description = "Converts the translation csv back to string resources."

    val projectRootDir = project.rootDir
    doLast {
        TranslationUtil.joinUpdateTranslation(projectRootDir)
    }
}

publishing {
    publications {
        create<MavenPublication>("binaryRelease") {
            groupId = "de.visualdigits"
            artifactId = "msfs2024tools"
            version = installerVersion

            val rootDir = project.rootDir

            // pdf docs
            artifact(tasks.named<org.asciidoctor.gradle.jvm.pdf.AsciidoctorPdfTask>("asciidoctorPdf").map { it.outputDir.resolve("README.pdf") }) {
                extension = "pdf"
                classifier = "docs"
            }

            // windows zip file
            artifact(tasks.named<Zip>("zip").flatMap { it.archiveFile }) {
                extension = "zip"
                classifier = "desktop"
            }

            // windows msi installer
            val msiFile = rootDir.walkTopDown().find { it.extension == "msi" }
            msiFile?.let { file ->
                artifact(file) {
                    extension = "msi"
                    classifier = "windows"
                }
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/sknull/News-Home-Reader")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

tasks.withType<PublishToMavenRepository> {
    // Desktop ZIP
    dependsOn(tasks.named("zip"))
    // PDF
    dependsOn(tasks.named("asciidoctorPdf"))

    // Native Installer (nur wenn sie auf dem OS existieren)
    dependsOn(tasks.matching { it.name == "packageMsi" })
    dependsOn(tasks.matching { it.name == "packageReleaseMsi" })
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}
