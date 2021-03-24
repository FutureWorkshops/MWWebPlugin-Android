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
                var token = ""
                token = if (project.rootProject.file("local.properties").exists()) {
                    properties.load(project.rootProject.file("local.properties").inputStream())
                    properties.getProperty("project.githubPAT")
                } else {
                    System.getenv("GITHUB_PAT") ?: ""
                }
                name = "Authorization"
                value = "token $token"
            }
            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }
    }
}

tasks.register("updateCoreVersion") {
    val versionRegex = Regex("project\\.coreVersion=[0-9.]{1,}")
    val newVersion = if (project.hasProperty("build.coreVersion")) {
        project.property("build.coreVersion")?.toString()
    } else {
        null
    }
    val file = File(rootDir, "gradle.properties")
    if (newVersion != null && file.exists()) {
        val content = file.readText()
        val newContent = versionRegex.replace(content, "project.coreVersion=$newVersion")
        file.writeText(newContent)
    }
}