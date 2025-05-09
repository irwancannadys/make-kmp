// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.9.20" apply false
    id("org.jetbrains.kotlin.multiplatform") version "1.9.20" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
    id("com.android.application") version "8.1.0" apply false
    id("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.compose") version "1.5.11" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.20" apply false
    // SQLDelight plugin is commented out as it requires additional setup
    // id("app.cash.sqldelight") version "2.0.0" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
