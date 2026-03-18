import de.visualdigits.translation.util.TranslationUtil
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.gradle.pdf)
}

version = "1.0.4"

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
        val outputFile = outputDirectory.file("AppConfig.kt").get().asFile
        outputFile.parentFile.mkdirs()
        outputFile.writeText("""
            package de.visualdigits.generated
            object AppConfig {
                const val VERSION = "${appVersion.get()}"
            }
        """.trimIndent())
    }
}

val generateVersionClass = tasks.register<GenerateVersionTask>("generateVersionClass") {
    appVersion.set(project.version.toString())
    outputDirectory.set(layout.buildDirectory.dir("generated/version"))
}

kotlin {
    jvm()
    jvmToolchain(21)

    sourceSets {
        val commonMain by getting {
            kotlin.srcDir(generateVersionClass)
        }

        commonMain.dependencies {
            implementation(libs.bundles.compose)
            implementation(libs.bundles.coil)
            implementation(libs.bundles.ktor)
            implementation(libs.bundles.koin)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation(libs.jetbrains.compose.navigation)

            implementation(libs.kotlin.xml.util)
            implementation(libs.kotlin.xml.serialization)
            implementation(libs.flatlaf)

            implementation(libs.kermit)
            implementation(libs.deskit)

            implementation(libs.html.converter)

        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.junit.jupiter.api)
            implementation(libs.junit.jupiter.engine)
            implementation(libs.junit.platform.launcher)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.skiko.awt.runtime.windows.x64)
            implementation(libs.kotlinx.coroutinesSwing)
        }

        jvmTest.dependencies {
        }
    }
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
            packageName = "de.visualdigits.msfs2024liverytools"
            packageVersion = project.version.toString()
            includeAllModules = false
            modules(
                "java.instrument", // Reflection
                "jdk.unsupported", // Wichtig für Skia-Grafik
                "java.desktop",    // ESSENZIELL für Fenster, Menü, Icons (Swing)
                "java.xml",        // Oft von Coil/Image-Parsern genutzt
                "java.naming",     // Von Koin/Dependency Injection benötigt
                "java.prefs"       // Falls du Einstellungen speicherst
            )
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            windows {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/Msfs2024Tools.ico"))
            }

//            buildTypes {
//                release {
//                    proguard {
//                        configurationFiles.from(project.file("proguard-rules.pro"))
//                        isEnabled.set(false)
//                        optimize.set(false)
//                    }
//                }
//            }
        }
    }
}

tasks.register("showDependencies") {
    doLast {
        configurations.kotlinCompilerClasspath.get()
            .forEach { println("#### ${it.canonicalPath}") }
    }
}

compose.resources {
    publicResClass = true
//    packageOfResClass = "com.dein.projekt.generated.resources"
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
    into(layout.buildDirectory.dir("compose/binaries/main/app/de.visualdigits.msfs2024liverytools"))
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

tasks.register<Zip>("zipDesktopDistributable") {
    group = "compose desktop"
    description = "Zippt das von createDistributable erzeugte Artefakt"

    dependsOn("createDistributable", copyPdfToDistribution, copyPdfToDocs)

    from(layout.buildDirectory.dir("compose/binaries/main/app"))
    from(tasks.asciidoctorPdf.map { it.outputDir }) {
        include("README.pdf") // oder "**/*.pdf"
        // Hier schiebst du es im ZIP an die richtige Stelle:
        into("de.visualdigits.msfs2024liverytools")
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
