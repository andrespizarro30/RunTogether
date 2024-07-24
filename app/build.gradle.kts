plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.afsoftwaresolutions.runtogether"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.afsoftwaresolutions.runtogether"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    viewBinding {
        enable = true
    }

    dataBinding {
        enable = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.google.android.material:material:1.4.0")

    val dagger_version = "2.51.1"
    implementation("com.google.dagger:dagger:$dagger_version")
    annotationProcessor("com.google.dagger:dagger-compiler:$dagger_version")

    implementation("com.google.dagger:dagger-android:$dagger_version")
    implementation("com.google.dagger:dagger-android-support:$dagger_version")
    annotationProcessor("com.google.dagger:dagger-android-processor:$dagger_version")

    val glide_version = "4.16.0"
    implementation("com.github.bumptech.glide:glide:$glide_version")
    annotationProcessor("com.github.bumptech.glide:compiler:$glide_version")

    val lifecycle_version = "2.2.0"
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.2")
    implementation("androidx.lifecycle:lifecycle-livedata:2.8.2")
    implementation("androidx.lifecycle:lifecycle-reactivestreams:2.8.2")

    val retrofit_version = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:${retrofit_version}")
    implementation("com.squareup.retrofit2:converter-gson:${retrofit_version}")

    val rxcalladapter_version = "2.9.0"
    implementation("com.squareup.retrofit2:adapter-rxjava2:$rxcalladapter_version")

    val rxandroid_version = "3.0.2"
    implementation("io.reactivex.rxjava3:rxandroid:$rxandroid_version")

    val rxjava_version = "3.1.5"
    implementation("io.reactivex.rxjava3:rxjava:$rxjava_version")

    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")

    implementation("com.google.firebase:firebase-database:20.2.2")

    implementation("com.google.android.gms:play-services-maps:18.2.0")

    implementation("com.squareup.picasso:picasso:2.8")

    implementation("com.google.maps.android:android-maps-utils:3.8.0")

}