// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.android.library") version "8.1.1" apply false
}

//subprojects {
//    dependencies {
//        classpath "com.google.dagger:hilt-android:2.44"
//        classpath "com.google.dagger:hilt-android-compiler:2.44"
//    }
//}

//subprojects {
//    apply {
//        plugin("kotlin-kapt")
//        plugin("com.google.dagger.hilt.android")
//    }
//
//    val implementation by configurations
//
//    dependencies {
//        implementation("com.google.dagger:hilt-android:2.44")
//        implementation("com.google.dagger:hilt-android-compiler:2.44")
//    }
//}
//subprojects {
//    repositories {
//        // Your repositories configuration
//    }
//}