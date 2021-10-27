import java.io.FileInputStream
import java.util.Properties
import org.gradle.api.publish.PublishingExtension

plugins {
    id("kotlin-multiplatform")
    id("org.jetbrains.dokka") version "1.4.0-rc"
    id("io.codearte.nexus-staging") version "0.30.0"
    `maven-publish`
    signing
}

enum class OS {
    LINUX, WINDOWS, MAC
}

fun getHostOsName(): OS =
    System.getProperty("os.name").let { osName ->
        when {
            osName == "Linux" -> OS.LINUX
            osName.startsWith("Windows") -> OS.WINDOWS
            osName.startsWith("Mac") -> OS.MAC
            else -> throw GradleException("Unknown OS: $osName")
        }
    }

kotlin {
    sourceSets {
        jvm()
        js() {
            browser()
            nodejs()
        }
        when (getHostOsName()) {
            OS.LINUX -> {
                linuxX64()
                linuxArm64()
            }
            OS.WINDOWS -> {
                mingwX64()
            }
            OS.MAC -> {
                macosX64()
                iosArm64()
                iosX64()
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(Libs.olekdia.common)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
        val jsMain by getting {
            dependencies {
            }
        }
        val nativeMain by creating {
            dependsOn(commonMain)
        }
        when (getHostOsName()) {
            OS.LINUX -> {
                val linuxX64Main by getting {
                    dependsOn(nativeMain)
                }
                val linuxArm64Main by getting {
                    dependsOn(nativeMain)
                }
            }
            OS.WINDOWS -> {
                val mingwX64Main by getting {
                    dependsOn(nativeMain)
                }
            }
            OS.MAC -> {
                val macosX64Main by getting {
                    dependsOn(nativeMain)
                }
                val iosArm64Main by getting {
                    dependsOn(nativeMain)
                }
                val iosX64Main by getting {
                    dependsOn(nativeMain)
                }
            }
        }
    }
}


tasks {
    create<Jar>("javadocJar") {
        dependsOn(dokkaJavadoc)
        archiveClassifier.set("javadoc")
        from(dokkaJavadoc.get().outputDirectory)
    }

    dokkaJavadoc {
        dokkaSourceSets {
            create("commonMain") {
                displayName = "common"
                platform = "common"
            }
        }
    }
}

//--------------------------------------------------------------------------------------------------
//  Publishing
//--------------------------------------------------------------------------------------------------

val fis = project.rootProject.file("local.properties").inputStream()
val properties = Properties().apply {
    load(fis)
}
val ossUser = properties.getProperty("oss.user")
val ossPassword = properties.getProperty("oss.password")
extra["signing.keyId"] = properties.getProperty("signing.keyId")
extra["signing.password"] = properties.getProperty("signing.password")
extra["signing.secretKeyRingFile"] = properties.getProperty("signing.secretKeyRingFile")

val libraryVersion: String by project
val publishedGroupId: String by project
val artifactName: String by project
val libraryName: String by project
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

signing {
    sign(publishing.publications)
}

afterEvaluate {
    configure<PublishingExtension> {
        publications.all {
            val mavenPublication = this as? MavenPublication
            mavenPublication?.artifactId =
                "${project.name}${"-$name".takeUnless { "kotlinMultiplatform" in name }.orEmpty()}"
        }
    }
}

publishing {
    publications.withType(MavenPublication::class) {
        groupId = publishedGroupId
        artifactId = artifactName
        version = libraryVersion

        artifact(tasks["javadocJar"])

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

    repositories {
        maven("https://oss.sonatype.org/service/local/staging/deploy/maven2/") {
            name = "sonatype"
            credentials {
                username = ossUser
                password = ossPassword
            }
        }
    }
}

nexusStaging {
    username = ossUser
    password = ossPassword
    packageGroup = publishedGroupId
}