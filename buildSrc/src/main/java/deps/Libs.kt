package deps

object Libs{
    object GradlePlugin{
        const val android = "com.android.tools.build:gradle:4.1.0"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72"
        const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:2.3.1"
        const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:9.4.1"
    }

    object Test {
        const val junitAPI = "org.junit.jupiter:junit-jupiter-api:5.6.1"
        const val junitEngine = "org.junit.jupiter:junit-jupiter-engine"
        const val testRunner = "androidx.test:runner:1.2.0"
    }

    object Timber {
        const val client = "com.jakewharton.timber:timber:4.7.1"
    }

    object Kotlin {
        const val stdlibJvm = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.72"
    }

    object AndroidX {
        const val appCompat = "androidx.appcompat:appcompat:1.1.0"
        const val recyclerView = "androidx.recyclerview:recyclerview:1.1.0"
        const val gridLayout = "androidx.gridlayout:gridlayout:1.0.0"
        const val constraint = "androidx.constraintlayout:constraintlayout:1.1.3"
        const val constraintSolver = "androidx.constraintlayout:constraintlayout-solver:1.1.3"

        const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:2.2.0"
        const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:2.2.0"

        object Navigation {
            const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:2.2.0"
            const val uiKtx = "androidx.navigation:navigation-ui-ktx:2.2.0"
        }
    }

    object RxJava{
        const val client = "io.reactivex.rxjava2:rxandroid:2.1.1"
    }

    object Dagger {
        const val client = "com.google.dagger:dagger:2.23.1"
        const val compiler = "com.google.dagger:dagger-compiler:2.23.1"
        const val android = "com.google.dagger:dagger-android:2.23.1"
        const val support = "com.google.dagger:dagger-android-support:2.23.1"
        const val processor = "com.google.dagger:dagger-android-processor:2.23.1"
    }

    object OkHttp{
        const val client = "com.squareup.okhttp3:okhttp:4.9.0"
    }

    object Retrofit {
        const val client = "com.squareup.retrofit2:retrofit:2.9.0"
        const val rxjava = "com.squareup.retrofit2:adapter-rxjava2:2.9.0"
        const val gson = "com.squareup.retrofit2:converter-gson:2.3.0"
    }

    object Gson {
        const val client = "com.google.code.gson:gson:2.8.6"
    }

}