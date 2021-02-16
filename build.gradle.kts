// Top-level build file where you can add configuration options common to all sub-projects/modules.
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

buildscript {
    val kotlin_version by extra("1.4.10")
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://jitpack.io") }


        maven {
            name = "Mobile Workflow"
            url = uri("https://raw.githubusercontent.com/FutureWorkshops/MobileWorkflowCore-Android-Distribution/main")
            credentials(HttpHeaderCredentials::class) {
                val properties = java.util.Properties()
                properties.load(project.rootProject.file("local.properties").inputStream())
                name = "Authorization"
                value = "token ${properties.getProperty("project.githubPAT")}"
            }
            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }
    }
}