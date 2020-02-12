plugins {
    id("kotlin-multiplatform")
}

kotlin {
    sourceSets {
        jvm()
        js() {
            browser()
        }
        linuxX64("native")
        // macosX64("native")

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
                implementation(Libs.kotlin.reflect)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(Libs.olekdia.common_jvm)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation(Libs.olekdia.common_js)
            }
        }
        val nativeMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(Libs.olekdia.common_native)
            }
        }
    }
}