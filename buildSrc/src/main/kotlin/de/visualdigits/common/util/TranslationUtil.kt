package de.visualdigits.common.util

import nl.adaptivity.xmlutil.serialization.XML
import java.io.File
import java.nio.file.Paths

object TranslationUtil {

    /**
     * Extracts translations in <PROJECT_ROOT>/composeApp/src/composeResources
     * to a csv file which is stored under <PROJECT_ROOT>/translations.
     *
     * Alongside the full csv additional txt files are stored for each language found.
     * Those contain all keys found across translations in the same order.
     * They are meant to be dropped into the translation website of your choice and pasted back.
     * Make sure that the lines are exactly same as the original before pasting them back.
     *
     * The sister method joinUpdateTranslation() will join those single files back to the csv and then
     * trigger the update of translations.
     */
    fun extractTranslation(
        rootDir: File
    ) {
        val stringResourcesDir = Paths.get(rootDir.canonicalPath, "composeApp", "src", "commonMain", "composeResources").toFile()

        val stringResources = stringResourcesDir
            .listFiles { f -> f.isDirectory && f.name.startsWith("values") }
            ?.associate { d ->
                var language = d.name.replace("values", "")
                language = if (language.isEmpty()) "default" else language.drop(1)
                val resources = XML.v1.invoke().decodeFromString<XmlResources>(File(d, "strings.xml").readText(), null)
                Pair(language, resources.strings.associate { s -> Pair(s.name, s.value) })
            } ?:mapOf()
        val languages = stringResources.keys.sorted()
        val missingEntries = languages.associateWith { sortedSetOf<String>() }
        val languageLists = languages.associateWith { mutableListOf<String>() }
        val allKeys = stringResources.values
            .flatMap { v -> v.keys }
            .distinct()
            .sorted()
        val rows = allKeys.map { key ->
            listOf(key) + languages.map { language ->
                val value = stringResources[language]?.get(key)?.unescapeStringResource()?:""
                if (value.isEmpty()) missingEntries[language]?.add(key)
                languageLists[language]?.add(value.replace("\n", " # ").replace("\\n", " # "))
                value
            }
        }

        val targetDir = Paths.get(rootDir.canonicalPath, "translation").toFile()
        if (!targetDir.exists()) {
            if(!targetDir.mkdirs()) error("Could not create targetDirectory '${targetDir.canonicalPath}'")
        }

        val keysFile = File(targetDir, "keys.txt")
        println("Writing keys to file: $keysFile ")
        keysFile.writeText(allKeys.joinToString("\n"))

        val missingEntriesFile = File(targetDir, "missing-entries.txt")
        println("Writing report to file: $missingEntriesFile ")
        missingEntriesFile.writeText("Missing Keys\n${"=".repeat(40)}\n\n${missingEntries.toList().joinToString("\n\n") { (language, keys) -> "$language\n${"-".repeat(40)}\n${keys.joinToString("\n")}" }}")

        val csv = "key;${languages.joinToString(";")}\n${rows.joinToString("\n") { row -> row.joinToString(";") } }"
        val csvFile = File(targetDir, "stringresources.csv")
        println("Writing string resources.csv: ${csvFile.canonicalPath}")
        csvFile.writeText(csv)

        languageLists.forEach { (language, values) ->
            val targetFile = File(targetDir, "stringresources-$language.txt")
            println("Writing string resources for langugae '$language' to file: ${targetFile.canonicalPath}")
            targetFile.writeText(values.joinToString("\n"))
        }
    }

    /**
     * Grabs the csv file stored under <PROJECT_ROOT>/translations and writes them back to valid
     * resource files for KMP.
     * When [overwriteExisting] is set to false (default is true) only empty entries are updated.
     */
    fun updateTranslation(
        rootDir: File
    ) {
        val stringResourcesDir = Paths.get(rootDir.canonicalPath, "composeApp", "src", "commonMain", "composeResources").toFile()
        val sourceFile = Paths.get(rootDir.canonicalPath, "translation", "stringresources.csv").toFile()
        val table = sourceFile
            .readLines()
            .map { line ->
                line.split(";(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)".toRegex())
                    .map { it.removeSurrounding("\"").replace("\"\"", "\"") }
            }
        val languages = table.take(1).first().drop(1)
        val resources = languages.map { XmlResources() }
        table
            .drop(1)
            .forEach { row ->
                val key = row.take(1).first()
                val values = row.drop(1)
                values.mapIndexed{ index, v ->
                    resources[index].strings.add(ResourceString(name = key, value = v.escapeStringResource()))
                }
            }
        resources.forEachIndexed { index, resource ->
            val language = languages[index]
            val dirName = if (language == "default") "values" else "values-$language"
            val targetDir = Paths.get(stringResourcesDir.canonicalPath, dirName).toFile()
            if (!targetDir.exists()) {
                if (!targetDir.mkdirs()) {
                    error("Could not create target directory: ${targetDir.canonicalPath}")
                }
            }
            val targetFile = File(targetDir, "strings.xml")
            println("Writing resource file: ${targetFile.canonicalPath}")
            targetFile.writeText(resource.writeValueAsXmlString(expandSelfClosingTags = true))
        }
    }

    /**
     * Grabs the single text files stored under <PROJECT_ROOT>/translations and synthesizes them back
     * to the csv file which joins all translations.
     * Afterward method updateTranslation() is called to update the translations for KMP.
     * When [overwriteExisting] is set to false (default is true) only empty entries are updated.
     */
    fun joinUpdateTranslation(
        rootDir: File
    ) {
        val sourceDir = Paths.get(rootDir.canonicalPath, "translation").toFile()
        val targetFile = File(sourceDir, "stringresources.csv")
        val languages = targetFile.readLines().take(1).first().split(";").drop(1)
        val resourceKeys = File(sourceDir, "keys.txt").readLines()
        val expectedNumberOfRows = resourceKeys.size

        val sb = StringBuilder()
        val data = sourceDir
            .listFiles { f -> f.isFile && f.name.startsWith("stringresources") && f.name.endsWith(".txt") }
            ?.associate { f ->
                val language = f.name
                    .replace("stringresources-", "")
                    .dropLast(4)
                val lines = f.readLines()
                if (lines.size != expectedNumberOfRows) error("Single file '${f.name}' has not the expected number of lines - not joining!")
                Pair(language, resourceKeys.zip(lines).toMap())
            }?:mapOf()
        sb.append("key;${languages.joinToString(";")}").append("\n")
        resourceKeys.forEach { resourceKey ->
            sb.append((listOf(resourceKey) + languages.map { language ->
                data[language]?.get(resourceKey)?.replace(" # ", "\\n")?:""
            }).joinToString(";")).append("\n")
        }
        println("Writing string resources.csv: ${targetFile.canonicalPath}")
        targetFile.writeText(sb.toString())
        updateTranslation(rootDir)
    }

    /**
     * Takes care about some special chars before serialize them back to xml.
     */
    private fun String.escapeStringResource(): String {
        if (this.isEmpty()) return ""

        return this
            // Special chars for KMP
            .replace("'", "\\'")      // Apostroph -> \'
            .replace("\"", "\\\"")    // Anführungszeichen -> \"
            .replace("%", "%%")       // Prozent -> %% (wegen String.format)
            .replace("\n", "\\n")

            // Special cases for KMP at the string start
            .let { s ->
                if (s.startsWith("?") || s.startsWith("@")) {
                    "\\" + s          // ? -> \? and @ -> \@
                } else s
            }

            // White space protection
            .let { s ->
                if (s.startsWith(" ") || s.endsWith(" ")) {
                    "\"$s\""          // Trailing and leading spaces -> " string "
                } else s
            }
    }

    /**
     * Takes care about some special chars before deserializing from xml.
     */
    private fun String.unescapeStringResource(): String {
        return this
            // Remove Android-Backslashes
            .replace("\\'", "'")
            .replace("\\\"", "\"")
            .replace("\\?", "?")
            .replace("\\@", "@")
            .replace("\n", "\\n")

            // resolve double quotes
            .replace("%%", "%")

            // Remove white space protectionn
            .let { if (it.startsWith("\"") && it.endsWith("\"")) it.removeSurrounding("\"") else it }
    }
}
