// shared/build.gradle.kts

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import org.gradle.api.publish.maven.MavenPublication

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("maven-publish")
    id("org.openapi.generator") version "7.6.0"
}

// Coordinates for ALL publications from this module
group = "com.example.esimsdkkmp"
version = "1.0.1"

kotlin {
    // Android target
    androidTarget {
        publishLibraryVariants("release")

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }

    // iOS + XCFramework
    val xcFramework = XCFramework("CardKitShared")

    // Keep references to the iOS targets so we can attach their source sets later
    val iosArm64Target = iosArm64()
    val iosSimArm64Target = iosSimulatorArm64()

    listOf(
        iosArm64Target,
        iosSimArm64Target
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "CardKitShared"
            isStatic = true
            xcFramework.add(this)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation("io.ktor:ktor-client-core:2.3.12")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")

                // kotlinx.serialization JSON
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
            }
            kotlin.srcDir("$projectDir/build/openapi/src/commonMain/kotlin")
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.lifecycle.viewmodel.ktx)
                implementation("io.ktor:ktor-client-android:2.3.12")
            }
        }

        // These actually exist because you called iosArm64() and iosSimulatorArm64()
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        // Create a shared iOS source set that both iOS targets depend on
        val iosMain by creating {
            dependsOn(commonMain)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.3.12")
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

openApiGenerate {
    // 1) Where the YAML is
    inputSpec.set("$projectDir/openapi/dadjoke.yaml")

    // 2) Which generator
    generatorName.set("kotlin")
    library.set("multiplatform")

    // 3) Where to put generated code
    outputDir.set("$projectDir/build/openapi")

    // 4) Packages
    packageName.set("com.esimsdkkmp.jokes")
    apiPackage.set("com.esimsdkkmp.jokes.api")
    modelPackage.set("com.esimsdkkmp.jokes.model")

    // 5) extra options
    additionalProperties.set(
        mapOf(
            "library" to "multiplatform",
            "dateLibrary" to "kotlinx-datetime"
        )
    )

    // helpful for debugging
    logToStderr.set(true)
    validateSpec.set(true)
}

tasks.named("compileKotlinMetadata") {
    dependsOn("openApiGenerate")
}
