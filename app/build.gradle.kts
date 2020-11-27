import deps.Libs

plugins {
    id("com.android.application")
//    kotlin("jvm")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
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
                val newApkName = "${applicationName}_${defaultConfig.versionCode}.apk"
                output.outputFileName = newApkName
            }
    }

    ktlint {
        version.set("0.22.0")
        debug.set(true)
        android.set(true)
        ignoreFailures.set(true)
    }

    tasks.preBuild {
        dependsOn(tasks.ktlintFormat)
    }

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
    implementation(deps.Libs.AndroidX.gridLayout)

    //test
    testImplementation(deps.Libs.Test.junitAPI)
    testRuntimeOnly(deps.Libs.Test.junitEngine)
    androidTestImplementation(deps.Libs.Test.testRunner)

    //timber
    implementation(deps.Libs.Timber.client)

    //kotlin
    implementation(deps.Libs.Kotlin.stdlibJvm)

    //android design
    implementation(deps.Libs.AndroidX.appCompat)
    implementation(deps.Libs.AndroidX.recyclerView)
    implementation(deps.Libs.AndroidX.constraint)
    implementation(deps.Libs.AndroidX.constraintSolver)

    //firebase
//    implementation "com.google.firebase:firebase-core:17.3.0"
//    implementation "com.google.firebase:firebase-perf:19.0.5"
//    implementation "com.google.firebase:firebase-core:17.4.1"
//    implementation "com.google.firebase:firebase-perf:19.0.7"

//    implementation "com.crashlytics.sdk.android:crashlytics:2.10.1"

    //rxjava2
    implementation(deps.Libs.RxJava.client)
//    implementation("com.github.tbruyelle:rxpermissions:0.10.2")

    //dagger
    implementation(deps.Libs.Dagger.client)
    kapt(deps.Libs.Dagger.compiler)
    implementation(deps.Libs.Dagger.android)
    implementation(deps.Libs.Dagger.support)
    kapt(deps.Libs.Dagger.processor)

    //lifecycle
    implementation(deps.Libs.AndroidX.lifecycleExtensions)
    kapt(deps.Libs.AndroidX.lifecycleCompiler)

    //navigation
    implementation(deps.Libs.AndroidX.Navigation.uiKtx)
    implementation(deps.Libs.AndroidX.Navigation.fragmentKtx)

    //okHttp
    implementation(deps.Libs.OkHttp.client)

    //retrofit
    implementation(deps.Libs.Retrofit.client)
    implementation(deps.Libs.Retrofit.gson)
    implementation(deps.Libs.Retrofit.rxjava)

    //gson
    implementation(deps.Libs.Gson.client)

    //recyclerView
    implementation(deps.Libs.AndroidX.recyclerView)
}
