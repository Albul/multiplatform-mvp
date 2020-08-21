import java.io.FileInputStream
import java.util.*
import org.gradle.api.publish.PublishingExtension

apply(plugin = "maven-publish")

val fis = FileInputStream("local.properties")
val properties = Properties().apply {
    load(fis)
}
val bintrayUser = properties.getProperty("bintray.user")
val bintrayApiKey = properties.getProperty("bintray.apikey")
val bintrayPassword = properties.getProperty("bintray.gpg.password")
val libraryVersion: String by project
val publishedGroupId: String by project
val artifactName: String by project
val bintrayRepo: String by project
val libraryName: String by project
val bintrayName: String by project
val libraryDescription: String by project
val siteUrl: String by project
val gitUrl: String by project
val licenseName: String by project
val licenseUrl: String by project
val developerOrg: String by project
val developerName: String by project
val developerEmail: String by project
val developerId: String by project

project.group = publishedGroupId
project.version = libraryVersion

afterEvaluate {
    configure<PublishingExtension> {
        publications.all {
            val mavenPublication = this as? MavenPublication
            mavenPublication?.artifactId =
                "${project.name}${"-$name".takeUnless { "kotlinMultiplatform" in name }.orEmpty()}"
        }
    }
}

configure<PublishingExtension> {
    publications {
        withType<MavenPublication> {
            groupId = publishedGroupId
            artifactId = artifactName
            version = libraryVersion

            pom {
                name.set(libraryName)
                description.set(libraryDescription)
                url.set(siteUrl)

                licenses {
                    license {
                        name.set(licenseName)
                        url.set(licenseUrl)
                    }
                }
                developers {
                    developer {
                        id.set(developerId)
                        name.set(developerName)
                        email.set(developerEmail)
                    }
                }
                organization {
                    name.set(developerOrg)
                }
                scm {
                    connection.set(gitUrl)
                    developerConnection.set(gitUrl)
                    url.set(siteUrl)
                }
            }
        }
    }

    repositories {
        maven("https://api.bintray.com/maven/${developerOrg}/${bintrayRepo}/${artifactName}/;publish=1") {
            credentials {
                username = bintrayUser
                password = bintrayApiKey
            }
        }
    }
}