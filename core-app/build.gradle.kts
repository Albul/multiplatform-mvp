plugins {
    id("kotlin-multiplatform")
}

kotlin {
    sourceSets {
        jvm()
        js() {
            browser()
        }
        linuxX64()

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(Libs.olekdia.common)
                implementation(project(":multiplatform-mvp"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(Libs.mockk_common)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(Libs.olekdia.common_jvm)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation(Libs.mockk_jvm)
            }
        }
    }
}