import deps.Libs

plugins {
    id("com.android.application")
//    kotlin("jvm") version "1.4.0"
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
//    id("androidx.navigation.safeargs.kotlin")
    id("org.jlleitschuh.gradle.ktlint")
    id("jacoco")
}

jacoco {
    toolVersion = "0.8.5"
//    reportsDir = file("$buildDir/JacocoReports")
}

//run "./gradlew test jacocoTestReport" and then genarated report of coverage
task("jacocoTestReport", JacocoReport::class) {
    dependsOn("testFreeDebugUnitTest")
    group = "Reporting"
    description = "Generate Jacoco coverage reports for the build. Only unit tests."
    reports {
        xml.isEnabled = true
        html.isEnabled = true
        csv.isEnabled = false
        xml.destination = file("$buildDir/reports/jacoco/report.xml")
        html.destination = file("$buildDir/reports/jacoco/html")
    }
    afterEvaluate {
        val fileFilter = setOf("**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*", "**/*Test*.*", "android/**/*.*")
        val debugTree = fileTree("$buildDir/tmp/kotlin-classes/freeDebug") {
            setExcludes(fileFilter)
        }
        sourceDirectories.setFrom("$projectDir/src/main/java")
        executionData.setFrom("$buildDir/jacoco/testFreeDebugUnitTest.exec")
        classDirectories.setFrom(debugTree)
    }
}

val applicationName = "Reversi"

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.2")

    defaultConfig {
        applicationId = "jp.mkjiro.reversi"
        minSdkVersion(29)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

//        val localProperty = property()
//        localProperty.load(project.rootProject.file("local.properties"))
    }

    signingConfigs {
//        free {
//            apply plugin: "com.google.gms.google-services"
//            apply plugin: "io.fabric"
//            keyAlias "key"
//            keyPassword "password"
//            storeFile file("../keys/abc.keystore")
//            storePassword "password"
//        }
//        paid {
//            keyAlias "key"
//            keyPassword "password"
//            storeFile file("../keys/abc.keystore")
//            storePassword "password"
//        }
    }

    flavorDimensions("Templete")

    productFlavors {
        create("free") {
            dimension = "Templete"
//            signingConfig signingConfigs.free
        }
        create("paid") {
            dimension = "Templete"
//            signingConfig signingConfigs.paid
        }
    }

    buildTypes {
        getByName("debug") {
            minifyEnabled(false)
            isTestCoverageEnabled = true
//                proguardFiles getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
        }
        //./gradlew clean assembleRelease
        getByName("release") {
            signingConfig = null
            minifyEnabled(false)
//                proguardFiles getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
    }

    applicationVariants.all {
        //./gradlew clean assembleRelease build staging and production at same time
        val variant = this
        variant.outputs
            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                val newApkName = "${applicationName}_${defaultConfig.versionName}.apk"
                output.outputFileName = newApkName
            }
    }

    ktlint {
        version.set("0.22.0")
        debug.set(true)
        android.set(true)
        ignoreFailures.set(true)
    }

//    tasks.preBuild {
//        dependsOn(tasks.ktlintFormat)
//    }

    // for junit5
    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.all {
            it.useJUnitPlatform()
            jacoco {
//                includeNoLocationClasses = true
            }
        }
    }
}

dependencies {
    implementation(Libs.AndroidX.gridLayout)

    //test
    testImplementation(Libs.Test.junitAPI)
    testRuntimeOnly(Libs.Test.junitEngine)
    androidTestImplementation(Libs.Test.testRunner)

    //timber
    implementation(Libs.Timber.client)

    //kotlin
    implementation(Libs.Kotlin.stdlibJvm)

    //android design
    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.recyclerView)
    implementation(Libs.AndroidX.constraint)
    implementation(Libs.AndroidX.constraintSolver)

    //firebase
//    implementation "com.google.firebase:firebase-core:17.3.0"
//    implementation "com.google.firebase:firebase-perf:19.0.5"
//    implementation "com.google.firebase:firebase-core:17.4.1"
//    implementation "com.google.firebase:firebase-perf:19.0.7"

//    implementation "com.crashlytics.sdk.android:crashlytics:2.10.1"

    //rxjava2
    implementation(Libs.RxJava.client)
//    implementation("com.github.tbruyelle:rxpermissions:0.10.2")

    //dagger
    implementation(Libs.Dagger.client)
    kapt(Libs.Dagger.compiler)
    implementation(Libs.Dagger.android)
    implementation(Libs.Dagger.support)
    kapt(Libs.Dagger.processor)

    //lifecycle
    implementation(Libs.AndroidX.lifecycleExtensions)
    kapt(Libs.AndroidX.lifecycleCompiler)

    //navigation
    implementation(Libs.AndroidX.Navigation.uiKtx)
    implementation(Libs.AndroidX.Navigation.fragmentKtx)

    //okHttp
    implementation(Libs.OkHttp.client)

    //retrofit
    implementation(Libs.Retrofit.client)
    implementation(Libs.Retrofit.gson)
    implementation(Libs.Retrofit.rxjava)

    //gson
    implementation(Libs.Gson.client)

    //recyclerView
    implementation(Libs.AndroidX.recyclerView)

    //coroutines
//    implementation(Libs.Coroutines.core)
}
