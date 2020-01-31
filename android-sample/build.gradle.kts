plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "com.olekdia.sample"
        minSdkVersion(17)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "org.mockito.junit.MockitoJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk7", "1.3.61"))
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("com.google.android.material:material:1.1.0-rc02")
    implementation("com.olekdia:android-common:3.1.3")
    implementation("com.olekdia:fam:3.0.2")
    implementation(project(":mvp"))

    testImplementation("junit:junit:4.13")
    testImplementation("org.mockito:mockito-core:3.2.4")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:1.3.61")
}