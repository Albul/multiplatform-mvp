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
                implementation("com.olekdia:multiplatform-common:0.1.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.61")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation("com.olekdia:multiplatform-common-jvm:0.1.1")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation("com.olekdia:multiplatform-common-js:0.1.1")
            }
        }
        val nativeMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation("com.olekdia:multiplatform-common-native:0.1.1")
            }
        }
    }
}