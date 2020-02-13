object Versions {
    val kotlin = "1.3.61"
    val mockk = "1.9.3"
    val junit = "4.13"
    val robolectric = "4.3.1"

    object androidx {
        val appcompat = "1.1.0"
        val material = "1.2.0-alpha04"
        val test_core = "1.2.0"
        val test_runner = "1.2.0"
        val test_rules = "1.2.0"
        val espresso_core = "3.2.0"
        val fragment_testing = "1.2.1"
    }

    object olekdia {
        val common = "0.1.1"
        val common_android = "3.1.3"
        val fam = "3.0.2"
        val materialdialog_core = "3.3.1"
    }

    val android_gradle = "3.5.3"
    val bintray_gradle = "1.8.4"
}


object Libs {
    object kotlin {
        val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    }
    val mockk_common = "io.mockk:mockk-common:${Versions.mockk}"
    val mockk_jvm = "io.mockk:mockk:${Versions.mockk}"
    val junit = "junit:junit:${Versions.junit}"
    val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"

    object androidx {
        val appcompat = "androidx.appcompat:appcompat:${Versions.androidx.appcompat}"
        val material = "com.google.android.material:material:${Versions.androidx.material}"
        val test_core = "androidx.test:core:${Versions.androidx.test_core}"
        val test_runner = "androidx.test:runner:${Versions.androidx.test_runner}"
        val test_rules = "androidx.test:rules:${Versions.androidx.test_rules}"
        val espresso_core = "androidx.test.espresso:espresso-core:${Versions.androidx.espresso_core}"
        val fragment_testing = "androidx.fragment:fragment-testing:${Versions.androidx.fragment_testing}"
    }
    object olekdia {
        private val common_prefix = "com.olekdia:multiplatform-common"
        val common = "$common_prefix:${Versions.olekdia.common}"
        val common_jvm = "$common_prefix-jvm:${Versions.olekdia.common}"
        val common_js = "$common_prefix-js:${Versions.olekdia.common}"
        val common_native = "$common_prefix-native:${Versions.olekdia.common}"
        val common_android = "com.olekdia:android-common:${Versions.olekdia.common_android}"
        val fam = "com.olekdia:fam:${Versions.olekdia.fam}"
        val materialdialog_core = "com.olekdia.material-dialog:core:${Versions.olekdia.materialdialog_core}"
    }

    object plugin {
        val kotlin_gradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
        val android_gradle = "com.android.tools.build:gradle:${Versions.android_gradle}"
        val bintray_gradle = "com.jfrog.bintray.gradle:gradle-bintray-plugin:${Versions.bintray_gradle}"
    }
}