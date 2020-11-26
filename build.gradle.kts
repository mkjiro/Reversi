// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlin_version by extra("1.3.72")
    val ktlint_version by extra("9.4.1")
    val kotlin_coroutines_version by extra("1.1.1")
    val work_version by extra("2.0.1")
    val dagger_version by extra("2.23.1")
    val support_version by extra("1.0.2")
    val navigation_version by extra("2.2.2")
    val rxandroid_version by extra("2.1.1")
    val rxkotlin_version by extra("2.3.0")
    val retrofit_version by extra("2.8.1")
    val okhttp_version by extra("4.6.0")
    val glide_version by extra("4.10.0")
    val junit_version by extra("5.7.0")

    repositories {
        google()
        jcenter()
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.0")
        classpath(kotlin("gradle-plugin", version = kotlin_version))
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navigation_version")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:9.4.1")
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