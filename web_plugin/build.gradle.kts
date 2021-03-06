plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("maven-publish")
    kotlin("android")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    defaultConfig {
        versionCode = Integer.parseInt(project.property("project.buildnumber")?.toString())
        versionName = project.property("project.buildversion")?.toString()
        minSdkVersion(24)
        targetSdkVersion(30)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
        }

        getByName("release") {
            isMinifyEnabled = true
            isDebuggable = false
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
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components.getByName("release"))
                groupId = project.property("project.bundleId")?.toString()
                version = project.property("project.buildversion")?.toString()
                artifactId = "web"

                pom {
                    name.set("Mobile Workflow")
                    url.set("https://www.mobileworkflow.io")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
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

dependencies {
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.3.0")

    implementation("io.reactivex.rxjava3:rxjava:3.0.4")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation("io.reactivex.rxjava3:rxkotlin:3.0.0")

    val fromMaven = project.property("project.mavenCore")?.toString()?.toBoolean() ?: false
    if (findProject(":mw-core") == null || fromMaven) {
        val version = project.property("project.coreVersion")
        implementation("com.futureworkshops.mobileworkflow:mw-core:$version")
    } else {
        implementation(project(":mw-core"))
    }
}
