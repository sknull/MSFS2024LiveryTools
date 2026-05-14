package de.visualdigits.msfs2024tools.data.model.msfs2024.usersettings


import de.visualdigits.msfs2024tools.data.model.msfs2024.common.Package
import de.visualdigits.msfs2024tools.data.model.msfs2024.common.Packages
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import java.io.File


@Serializable
class UserSettings(
    @XmlSerialName("CheckedOutPackages")
    val checkedOutPackages: Packages? = null,

    @XmlSerialName("SelectedPackages")
    val selectedPackages: Packages? = null,

    @XmlSerialName("Filter")
    @XmlElement(true)
    val filter: String? = null,

    @XmlSerialName("ShowOnlyEdited")
    @XmlElement(true)
    val showOnlyEdited: Boolean? = false
) {

    companion object {

        val USER_SETTINGS_DEFAULT =
            UserSettings(
                checkedOutPackages = Packages(
                    listOf(
                        Package(
                            name = "png-2-ktx2"
                        )
                    )
                ),
                selectedPackages = Packages(
                    listOf(
                        Package(
                            name = "png-2-ktx2"
                        )
                    )
                ),
                showOnlyEdited = false
            )

        fun readValue(file: File): UserSettings {
            return try {
                XML.v1.invoke().decodeFromString(file.readText(), null)
            } catch (e: Exception) {
                throw IllegalStateException("Could not parse file '$file'", e)
            }
        }
    }
}
