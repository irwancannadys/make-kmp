plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.9.20"
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true  // Digunakan agar framework statis (recommended)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Compose Multiplatform
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                
                // Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                
                // Ktor - HTTP Client
                implementation("io.ktor:ktor-client-core:2.3.7")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
                implementation("io.ktor:ktor-client-logging:2.3.7")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
                
                // Serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
                
                // DateTime
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
                
                // Multiplatform Settings - SharedPreferences/UserDefaults
                implementation("com.russhwolf:multiplatform-settings:1.1.1")
                implementation("com.russhwolf:multiplatform-settings-no-arg:1.1.1")
                
                // Koin for DI
                implementation("io.insert-koin:koin-core:3.5.0")
                
                // Multiplatform Navigation
                implementation("moe.tlaster:precompose:1.5.7")
                
                // SQL Delight will be commented out as it requires a gradle plugin 
                // implementation("app.cash.sqldelight:runtime:2.0.0")
                
                // Image Loading (commented out as it might not be compatible with all platforms)
                // implementation("io.github.qdsfdhvh:image-loader:1.6.7")
            }
        }
        
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
                implementation("io.insert-koin:koin-test:3.5.0")
                implementation("app.cash.turbine:turbine:1.0.0") // For Flow testing
            }
        }
        
        val androidMain by getting {
            dependencies {
                // Android specific dependencies
                implementation("androidx.appcompat:appcompat:1.6.1")
                implementation("androidx.activity:activity-compose:1.8.1")
                implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
                
                // Koin for Android
                implementation("io.insert-koin:koin-android:3.5.0")
                
                // Ktor Android Engine
                implementation("io.ktor:ktor-client-android:2.3.7")
                implementation("io.ktor:ktor-client-okhttp:2.3.7")
                
                // AndroidX Security Crypto (for secure storage)
                implementation("androidx.security:security-crypto:1.0.0")
                
                // WorkManager for background tasks
                implementation("androidx.work:work-runtime-ktx:2.8.1")
                
                // Room will be commented out as it requires annotation processor setup
                // implementation("androidx.room:room-runtime:2.6.0")
            }
        }
        
        val androidUnitTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
                implementation("androidx.test:core:1.5.0")
                implementation("org.robolectric:robolectric:4.11.1")
            }
        }
        
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by getting {
            dependencies {
                // iOS specific dependencies
                implementation("io.ktor:ktor-client-darwin:2.3.7")
            }
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by getting {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "{{PACKAGE_NAME}}"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
