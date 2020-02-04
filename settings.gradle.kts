rootProject.name = "mvp-multiplatform"

include(
        ":multiplatform-mvp",
        ":core-app",
        ":android-app"
)

enableFeaturePreview("GRADLE_METADATA")