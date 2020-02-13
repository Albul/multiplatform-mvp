# Multiplatform common

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) 
[ ![Download](https://api.bintray.com/packages/olekdia/olekdia/multiplatform-mvp/images/download.svg?version=0.1.0) ](https://bintray.com/olekdia/olekdia/multiplatform-mvp/0.1.0/link)

---

Kotlin multiplatform model-view-presenter framework.
This small in terms of API size framework allows you to create testable model and presenter abstractions, with view implementations on any platform.


### Setup

To use in multiplatform project add:

```gradle
dependencies {
  ...
  implementation("com.olekdia:mvp-common:0.1.0")
}
```

To use in platform specific projects include one of the following:
```gradle
dependencies {
  implementation("com.olekdia:mvp-common-jvm:0.1.0")
  implementation("com.olekdia:mvp-common-js:0.1.0")
  implementation("com.olekdia:mvp-common-native:0.1.0")
}
```

Make sure your Gradle vesion is 5.3+, and that you have metadata enabled in settings.gradle

```gradle
enableFeaturePreview("GRADLE_METADATA")
```