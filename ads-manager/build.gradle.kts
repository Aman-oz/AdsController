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
        jvmTarget = "17"
    }
}

/*afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "com.github.Aman-oz"
                artifactId = "ads-manager"
                version = "1.0.3"

                from(components["release"])
            }
        }
    }
}*/


    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "com.github.Aman-oz"
                artifactId = "ads-manager"
                version = "1.0.3"

                /*pom {
                    name = "Preference"
                    description = "Android preference extensions"
                    url = "https://github.com/Slion/Preference"
                    *//*properties = mapOf(
                        "myProp" to "value",
                        "prop.with.dots" to "anotherValue"
                    )*//*
                    licenses {
                        license {
                            name = "GNU Lesser General Public License v3.0"
                            url = "https://github.com/Slion/Preference/blob/main/LICENSE"
                        }
                    }
                    developers {
                        developer {
                            id = "Slion"
                            name = "Stéphane Lenclud"
                            email = "github@lenclud.com"
                        }
                    }
                    scm {
                        //connection = "scm:git:git://example.com/my-library.git"
                        //developerConnection = "scm:git:ssh://example.com/my-library.git"
                        url = "https://github.com/Slion/Preference"
                    }
                }*/

                afterEvaluate {
                    from(components["release"])
                }
            }
        }

        // That gives us a task named publishAllPublicationsToMavenRepository
        repositories {
            maven {
                name = "maven"
                url = uri(layout.buildDirectory.dir("maven"))
            }
        }
    }



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


