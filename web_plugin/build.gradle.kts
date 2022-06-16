plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("maven-publish")
    kotlin("android")
}

android {
    compileSdk = 31
    buildToolsVersion = "31.0.0"

    defaultConfig {
        minSdk = 24
        targetSdk = 31

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "PLUGIN_VERSION", "\"${project.property("project.buildversion")?.toString()}\"")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()

    buildTypes {
        debug {
            isMinifyEnabled = false
        }

        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    sourceSets {
        getByName("test") {
            resources.srcDirs("src/test/res")
        }
    }

    buildFeatures {
        viewBinding = true
    }

    testOptions.unitTests.isReturnDefaultValues = true
    testOptions.unitTests.isIncludeAndroidResources = true
    packagingOptions {
        dex {
            useLegacyPackaging = false
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components.getByName("release"))
                groupId = project.property("project.bundleId")?.toString()
                version = project.property("project.buildversion")?.toString()
                artifactId = project.property("project.artifactId")?.toString()

                pom {
                    name.set("App Rail")
                    url.set("https://app-rail.io")
                    licenses {
                        license {
                            distribution.set("repo")
                            comments.set("© 2022 Future Workshops Limited, All rights reserved")
                        }
                    }
                    developers {
                        developer {
                            id.set("futureworkshops")
                            name.set("Future Workshops")
                            email.set("info@futureworkshops.com")
                        }
                    }
                }
            }
        }
        repositories {
            maven {
                val deployPath = project.property("project.deployPath")?.toString()
                url = uri(file(deployPath!!))
            }
        }
    }
}

tasks.register("copyOutputs", Copy::class) {
    from(project.buildDir)
    into(rootProject.buildDir)
}

dependencies {
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")

    implementation("io.reactivex.rxjava3:rxjava:3.1.3")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation("io.reactivex.rxjava3:rxkotlin:3.0.1")

    val fromMaven = project.property("project.mavenCore")?.toString()?.toBoolean() ?: false
    if (findProject(":mw-core") == null || fromMaven) {
        val version = project.property("project.coreVersion")
        implementation("com.futureworkshops.mobileworkflow:mw-core:$version")
    } else {
        implementation(project(":mw-core"))
    }
}
