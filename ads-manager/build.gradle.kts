plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("maven-publish")
//    alias(libs.plugins.mavenPublish)
}

android {
    namespace = "com.aman.ads_manager"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

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
                artifactId = "AdsController"
                version = "2.0.5"

                from(components["release"])
            }
        }
    }
}*/

publishing {
    publications {
        register<MavenPublication>("release") {
            afterEvaluate {
                from(components["release"])
                groupId = "com.github.Aman-oz"
                artifactId = "AdsController"
                version = "v2.0.8"
            }
        }
    }
}

/*publishing {
    publications {
        register("gprRelease", MavenPublication::class) {
            from(components["release"])
            groupId = "com.github.Aman-oz"
            artifactId = "AdsController"
            version = "v2.0.8"

        }
    }
}*/


    /*publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "com.github.Aman-oz"
                artifactId = "AdsController"
                version = "1.0.7"
                afterEvaluate {
                    from(components["release"])
                }

                *//*pom {
                    packaging = "aar"
                    name.set("foobar")
                    description.set("This library does things and stuff!")
                    url.set("https://github.com/example/foobar")
                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://opensource.org/license/mit/")
                        }
                    }
                    developers {
                        developer {
                            name.set("Aman")
                            email.set("aman.ozi@example.com")
                        }
                    }
                    scm {
                        url.set(pom.url.get())
                        connection.set("scm:git:${url.get()}.git")
                        developerConnection.set("scm:git:${url.get()}.git")
                    }
                }*//*

                *//*pom {
                    name = "Preference"
                    description = "Android preference extensions"
                    url = "https://github.com/Slion/Preference"
                    *//**//*properties = mapOf(
                        "myProp" to "value",
                        "prop.with.dots" to "anotherValue"
                    )*//**//*
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
                }*//*


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
*/


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

    implementation(libs.facebook.audience.network)
}


