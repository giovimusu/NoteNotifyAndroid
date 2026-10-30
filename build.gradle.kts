plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "it.notenotify.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "it.notenotify.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1"
    }
}
