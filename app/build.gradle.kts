plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.aman.adscontroller"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aman.adscontroller"
        minSdk = 24
        targetSdk = 34
        versionCode = 3
        versionName = "1.0.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false

            resValue ("string", "admob_inter_ids", "ca-app-pub-3940256099942544/1033173712")
            resValue ("string", "admob_rewarded_ids", "ca-app-pub-3940256099942544/5224354917")
            resValue ("string", "admob_rewarded_inter_ids", "ca-app-pub-3940256099942544/5354046379")
            resValue ("string", "admob_native_ids", "ca-app-pub-3940256099942544/2247696110")
            resValue ("string", "admob_banner_ids", "ca-app-pub-3940256099942544/2014213617")
            resValue ("string", "admob_app_open_ids", "ca-app-pub-3940256099942544/3419835294")
            resValue ("string", "admob_app_id", "ca-app-pub-3940256099942544~3347511713")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isMinifyEnabled = false
            isShrinkResources = false

            resValue ("string", "admob_inter_ids", "ca-app-pub-3940256099942544/1033173712")
            resValue ("string", "admob_rewarded_ids", "ca-app-pub-3940256099942544/5224354917")
            resValue ("string", "admob_rewarded_inter_ids", "ca-app-pub-3940256099942544/5354046379")
            resValue ("string", "admob_native_ids", "ca-app-pub-3940256099942544/2247696110")
            resValue ("string", "admob_banner_ids", "ca-app-pub-3940256099942544/2014213617")
            resValue ("string", "admob_app_open_ids", "ca-app-pub-3940256099942544/9257395921")
            resValue ("string", "admob_app_id", "ca-app-pub-3940256099942544~3347511713")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.layout.size.sdp)
    implementation(libs.text.size.ssp)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.koin.di)
    implementation(libs.workmanager)
    implementation(libs.google.ads)
    implementation(libs.shimmer.view)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.remote.config)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.androidx.lifecycle.process)
    //implementation(project(path = ":ads-manager"))
    implementation (libs.admob.ads.controller)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}