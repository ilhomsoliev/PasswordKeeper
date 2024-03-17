plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
}

android {
    namespace = "com.ilhomsoliev.passwordkeeper"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ilhomsoliev.passwordkeeper"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    androidBase()
    ktor()
    compose()
    paging()
    dataStore()
    room()
    kapt("androidx.room:room-compiler:2.6.0")
    implementation("androidx.room:room-ktx:$room")
    // Coil
    implementation("io.coil-kt:coil-compose:2.4.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    implementation("com.google.code.gson:gson:2.10.1")
    // Jetpack security
    implementation("androidx.security:security-crypto:1.1.0-alpha01")

    // Biometrics
    implementation("androidx.biometric:biometric:1.2.0-alpha01")
}