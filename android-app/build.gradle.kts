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
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    testOptions.unitTests.isIncludeAndroidResources = true

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
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

    testImplementation(Libs.junit)
    testImplementation(Libs.robolectric)
    testImplementation(Libs.androidx.test_core)
    testImplementation(Libs.androidx.test_runner)
    testImplementation(Libs.androidx.test_rules)
    testImplementation(Libs.androidx.espresso_core)
    testImplementation(Libs.mockk_jvm)

    debugImplementation(Libs.androidx.fragment_testing)
    testImplementation(Libs.androidx.fragment_testing)
}