plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.example.test1"
    compileSdk = 35
    //compileSdk = 34


    defaultConfig {
        applicationId = "com.example.test1"
        minSdk = 24
        targetSdk = 35
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

    /*composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"  // Must match Compose version
    }*/

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }
}

dependencies {

    //start santé
    // Firebase BOM (Bill of Materials) - manages versions for you
    implementation(platform(libs.firebase.bom))

    // Firebase dependencies (no versions needed when using BOM)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)

    // AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // RecyclerView (critical for your error)
    implementation(libs.androidx.recyclerview.v132)
    //implementation(libs.androidx.recyclerview)
    //xml dependecies
    implementation (libs.androidx.constraintlayout)
    // Lifecycle components
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Compose (only include if you're using Jetpack Compose)
  //fin de dependecies santé & start dependecies safaa
    // Firebase BOM (gère les versions automatiquement)
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))

    // Firebase Auth et Firestore
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    // firebase-storage
    implementation ("com.google.firebase:firebase-storage-ktx:20.3.0")


    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    /*implementation(libs.recyclerview)*/

    // Glide pour charger les images du restaurant
    implementation("com.github.bumptech.glide:glide:4.15.1")
    //implementation(libs.glide)


    // AndroidX de base
    //implementation(libs.androidxCoreKtx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // CardView
    implementation("androidx.cardview:cardview:1.0.0")
    //implementation(libs.cardview)

    // Lifecycle et Compose
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
  //cardview santé xml
    implementation(libs.androidx.cardview)

  



    implementation ("com.google.android.material:material:1.11.0")

    // Tests

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)



}

