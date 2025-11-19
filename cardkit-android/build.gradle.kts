// cardkit-android/build.gradle.kts

import org.gradle.api.publish.maven.MavenPublication

plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish")
}

// Coordinates for this Android wrapper artefact
group = "com.example.esimsdkkmp"
version = "1.0.1"

android {
    namespace = "com.example.cardkit_android"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }

    // 👇 IMPORTANT: this is what creates the 'release' software component
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // This is your KMP Android artefact that exposes CardsViewModel etc.
    api(project(":shared"))
    api(libs.kotlinx.coroutines.core)

}

/**
 * We must hook into afterEvaluate so that the Android plugin has already
 * registered the 'release' component. Otherwise components["release"]
 * will throw "SoftwareComponent with name 'release' not found".
 */
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = group.toString()
                artifactId = "cardkit-android"
                version = version.toString()
            }
        }
    }
}
