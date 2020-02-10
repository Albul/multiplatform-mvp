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
                implementation("com.olekdia:multiplatform-common:0.1.1")
                implementation(project(":multiplatform-mvp"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("io.mockk:mockk-common:1.9.3")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation("com.olekdia:multiplatform-common-jvm:0.1.1")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation("io.mockk:mockk:1.9.3")
            }
        }
    }
}