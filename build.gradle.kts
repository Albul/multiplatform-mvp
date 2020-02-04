val kotlin_version = "1.3.61"

buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61")
        classpath("com.android.tools.build:gradle:3.5.3")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
}