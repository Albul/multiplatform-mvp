plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "com.olekdia.mvpapp"
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
    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/license.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/*.kotlin_module")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk7", Versions.kotlin))
    implementation(Libs.androidx.appcompat)
    implementation(Libs.androidx.material)
    implementation(Libs.olekdia.common_jvm)
    implementation(Libs.olekdia.common_android)
    implementation(Libs.olekdia.fam)
    implementation(Libs.olekdia.materialdialog_core)
    implementation(project(":multiplatform-mvp", "jvmDefault"))
    implementation(project(":core-app", "jvmDefault"))
}