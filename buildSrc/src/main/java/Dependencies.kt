object Versions {
    val kotlin = "1.3.61"
    val mockk = "1.9.3"

    object androidx {
        val appcompat = "1.1.0"
        val material = "1.2.0-alpha04"
    }

    object olekdia {
        val common = "0.1.1"
        val common_android = "3.1.3"
        val fam = "3.0.2"
        val materialdialog_core = "3.3.1"
    }

    val android_gradle = "3.5.3"
}


object Libs {
    object kotlin {
        val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    }
    val mockk_common = "io.mockk:mockk-common:${Versions.mockk}"
    val mockk_jvm = "io.mockk:mockk:${Versions.mockk}"

    object androidx {
        val appcompat = "androidx.appcompat:appcompat:${Versions.androidx.appcompat}"
        val material = "com.google.android.material:material:${Versions.androidx.material}"
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
    }
}