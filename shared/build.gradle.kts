// shared/build.gradle.kts

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import org.gradle.api.publish.maven.MavenPublication

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("maven-publish")
}

// Coordinates for ALL publications from this module
group = "com.example.esimsdkkmp"
version = "1.0.1"

kotlin {
    // Android target for CardsViewModel, repositories, etc.
    androidTarget {
        // Ensure an Android-only artifact exists: shared-android
        publishLibraryVariants("release")

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }

    // iOS frameworks / XCFramework
    val xcFramework = XCFramework("CardKitShared")

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "CardKitShared"   // what the iOS integrator will see
            isStatic = true
            xcFramework.add(this)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val androidMain by getting {
            dependencies {
                // For CardsViewModel & other Android-specific bits
                implementation(libs.androidx.lifecycle.viewmodel.ktx)
            }
        }
    }
}

android {
    namespace = "com.example.esimsdkkmp.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // Optional but nice if you want sources JAR for the Android variant
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

// Let Kotlin MPP create all publications (multiplatform + android + ios),
// we just normalize groupId/version for safety (even though group/version
// is already set above).
publishing {
    publications.withType<MavenPublication>().configureEach {
        groupId = project.group.toString()    // com.example.esimsdkkmp
        version = project.version.toString()  // 1.0.1
    }
}
