plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("androidx.room") version "2.7.2" apply false
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android") version "2.57.1"
}

android {
    namespace = "com.autoparts"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.autoparts"
        minSdk = 26
        targetSdk = 36
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

//navegacion

    implementation("androidx.navigation:navigation-compose:2.9.3")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    implementation("androidx.compose.material3:material3:1.5.0-alpha08")

//room

    implementation("androidx.room:room-runtime:2.7.2")

    annotationProcessor("androidx.room:room-compiler:2.7.2")

    ksp("androidx.room:room-compiler:2.7.2")

// optional - Kotlin Extensions and Coroutines support for Room

    implementation("androidx.room:room-ktx:2.7.2")

//Hilt

    implementation("com.google.dagger:hilt-android:2.57.1")

    ksp("com.google.dagger:hilt-android-compiler:2.57.1")

    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
//retrofit

    implementation("com.squareup.retrofit2:retrofit:3.0.0")

    implementation("com.squareup.moshi:moshi-kotlin:1.15.2")

    implementation("com.squareup.retrofit2:converter-moshi:3.0.0")

    implementation("com.squareup.okhttp3:logging-interceptor:5.1.0")

    implementation(platform("androidx.compose:compose-bom:2024.10.00"))

    implementation("androidx.compose.material3:material3")

    // Íconos Material
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")

    // Coil para carga de imágenes
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Glide para mejor soporte de Base64 data URLs
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")
    
    // DataStore para persistencia de preferencias
    implementation("androidx.datastore:datastore-preferences:1.1.1")
}