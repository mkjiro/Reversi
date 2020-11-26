

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
    implementation("androidx.gridlayout:gridlayout:1.0.0")
//
    //test
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    androidTestImplementation("androidx.test:runner:1.2.0")

    //timber
    implementation("com.jakewharton.timber:timber:4.7.1")

    //kotlin
    implementation(kotlin("stdlib-jdk7:${rootProject.extra["kotlin_version"]}"))

    //android design
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.constraintlayout:constraintlayout-solver:1.1.3")

    //firebase
//    implementation "com.google.firebase:firebase-core:17.3.0"
//    implementation "com.google.firebase:firebase-perf:19.0.5"
//    implementation "com.google.firebase:firebase-core:17.4.1"
//    implementation "com.google.firebase:firebase-perf:19.0.7"

//    implementation "com.crashlytics.sdk.android:crashlytics:2.10.1"

    //rxjava2
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("com.github.tbruyelle:rxpermissions:0.10.2")

    //dagger
    implementation("com.google.dagger:dagger:${rootProject.extra["dagger_version"]}")
    kapt("com.google.dagger:dagger-compiler:${rootProject.extra["dagger_version"]}")
    implementation("com.google.dagger:dagger-android:${rootProject.extra["dagger_version"]}")
    implementation("com.google.dagger:dagger-android-support:${rootProject.extra["dagger_version"]}")
    kapt("com.google.dagger:dagger-android-processor:${rootProject.extra["dagger_version"]}")

    //lifecycle
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    //noinspection LifecycleAnnotationProcessorWithJava8
    kapt("androidx.lifecycle:lifecycle-compiler:2.2.0")

    //navigation
    implementation("androidx.navigation:navigation-ui-ktx:${rootProject.extra["navigation_version"]}")
    implementation("androidx.navigation:navigation-fragment-ktx:${rootProject.extra["navigation_version"]}")

    //okHttp
    implementation("com.squareup.okhttp3:okhttp:4.9.0")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.3.0")

    //gson
    implementation("com.google.code.gson:gson:2.8.6")

    //recyclerView
    implementation("androidx.recyclerview:recyclerview:1.1.0")
}
