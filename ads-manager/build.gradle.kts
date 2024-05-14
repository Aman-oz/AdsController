plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("maven-publish")
}

android {
    namespace = "com.aman.ads_manager"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        jvmTarget = "1.8"
    }
}
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "com.github.Aman-oz"
                artifactId = "ads-manager"
                version = "1.0.0"

                from(components["release"])
            }
        }
    }
}

/*afterEvaluate {
    android.libraryVariants.forEach { variant ->
        publishing.publications.create(variant.name, MavenPublication::class) {
            groupId = "com.github.Aman-oz"
            artifactId = "ads-manager"
            version = "1.0.0"

            from(components["release"])
        }
    }
}*/


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.google.ads)
    implementation(libs.shimmer.view)
    implementation(libs.layout.size.sdp)
    implementation(libs.text.size.ssp)
}

/*afterEvaluate {
    publishing {
        publications {
            release(MavenPublication) {
                from components.release
                groupId = "com.aman"
                artifactId = "ads-manager"
                version = "1.0.0"
            }
        }
    }
}*/

/*publishing {
    publications {
        register("gprRelease", MavenPublication::class) {
            from(components["release"])
            groupId = "com.aman"
            artifactId = "ads-manager"
            version = "1.0.0"

        }
    }
}*/


