plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "com.submission.zelsis"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.submission.zelsis"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        buildConfigField("String", "BASE_URL", "\"https://story-api.dicoding.dev/v1/\"")
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    configurations {
        implementation.get().exclude(mapOf("group" to "org.jetbrains", "module" to "annotations"))
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)



    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.converter.gson)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.circleimageview)
    implementation(libs.glide)
    implementation(libs.play.services.maps)
    implementation(libs.androidx.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.core.testing)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.mockito.inline)
    implementation(libs.mockito.core)
    implementation(libs.androidx.paging.runtime.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

