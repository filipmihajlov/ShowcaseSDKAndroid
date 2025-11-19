// shared/build.gradle.kts
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import org.gradle.api.publish.maven.MavenPublication
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.3.0-RC"
    id("org.openapi.generator") version "7.6.0"
    id("maven-publish")
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
                implementation("io.ktor:ktor-client-logging:2.3.12")
            }
            kotlin.srcDir("$buildDir/generated/openapi/src/commonMain/kotlin")
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

    // 2) Kotlin MPP client
    generatorName.set("kotlin")
    library.set("multiplatform")

    // 3) Packages (must match your imports like com.esimsdkkmp.jokes.api.DefaultApi)
    packageName.set("com.esimsdkkmp.jokes")
    apiPackage.set("com.esimsdkkmp.jokes.api")
    modelPackage.set("com.esimsdkkmp.jokes.model")
    invokerPackage.set("com.esimsdkkmp.jokes.invoker")

    // 4) Where code is generated
    // Resulting path: shared/build/generated/openapi/src/commonMain/kotlin/...
    outputDir.set("$buildDir/generated/openapi")

    // 5) Minimal config to avoid double @Serializable
    configOptions.set(
        mapOf(
            "dateLibrary" to "kotlinx-datetime"
        )
    )

    logToStderr.set(true)
    validateSpec.set(true)
}

// Make sure ALL Kotlin compiles run after OpenAPI generation
tasks.withType<KotlinCompilationTask<*>>().configureEach {
    dependsOn(tasks.named("openApiGenerate"))
}

// Make sure ALL source JARs run after OpenAPI generation
tasks.withType<Jar>().configureEach {
    dependsOn(tasks.named("openApiGenerate"))
}
