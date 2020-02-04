plugins {
    id("kotlin-multiplatform")
}

/*android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(14)
    }

    sourceSets {
        val main by getting {
            setRoot("src/androidMain")
        }
    }
}*/

kotlin {
    sourceSets {
        jvm()
        js() {
            browser()
        }
        //android("android")
        linuxX64("native")
        // macosX64("linux")

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("com.olekdia:multiplatform-common:0.1.1")
            }
        }
/*        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }*/
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }
        val nativeMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
//        val androidMain by getting {
//            dependencies {
//                implementation(kotlin("stdlib"))
//                implementation("androidx.appcompat:appcompat:1.1.0")
//                implementation("androidx.fragment:fragment:1.1.0")
//            }
//        }
    }
}