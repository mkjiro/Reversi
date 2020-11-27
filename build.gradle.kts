// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath(deps.Libs.GradlePlugin.android)
        classpath(deps.Libs.GradlePlugin.kotlin)
        classpath(deps.Libs.GradlePlugin.safeArgs)
        classpath(deps.Libs.GradlePlugin.ktlint)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "http://android.aldebaran.com/sdk/maven")
        maven(url = "https://jitpack.io")
    }
}

tasks.register("clean",Delete::class.java){
    delete(rootProject.buildDir)
}