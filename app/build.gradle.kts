// Remove the incorrect import if it's still there:
// import androidx.glance.appwidget.compose // <--- DELETE THIS LINE

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.googleKsp) // Apply KSP plugin
}

android {
    namespace = "com.example.todo" // Make sure this matches your package structure
    compileSdk = 35 // Consider using the latest stable SDK, e.g., 34, unless you need 35 features

    defaultConfig {
        applicationId = "com.example.todo"
        minSdk = 24
        targetSdk = 35 // Match compileSdk or latest stable
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    // composeOptions for specific Kotlin compiler extension version for Compose
    // (usually handled by the Compose BOM, but can be specified if needed)
    // composeOptions {
    //     kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get() // If you define compose compiler version in TOML
    // }
    packagingOptions { // Commonly needed with Compose
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // ViewModel for Compose
    implementation(libs.androidx.lifecycle.viewmodelCompose)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)      // For Kotlin Coroutines and Flow support
    ksp(libs.androidx.room.compiler)           // Room annotation processor with KSP

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
