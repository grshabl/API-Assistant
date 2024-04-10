plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android") version "2.51" apply true
    id("kotlin-parcelize")
    kotlin("kapt")
}

android {
    namespace = "com.example.domain"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")

    implementation("com.google.dagger:hilt-android:2.51")
    kapt("com.google.dagger:hilt-android-compiler:2.51")

    implementation("io.swagger.parser.v3:swagger-parser:2.1.21") {
        exclude(group="io.swagger", module="swagger-parser-safe-url-resolver")
        exclude(group="javax.validation", module="validation-api")
    }
    implementation("com.google.code.gson:gson:2.8.8")

    implementation("org.apache.commons:commons-text:1.9")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:4.8.1")
}