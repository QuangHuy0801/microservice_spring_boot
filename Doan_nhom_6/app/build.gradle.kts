plugins {
    id("com.android.application")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.example.doan_nhom_6"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.doan_nhom_6"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //========================================================================
    configurations.maybeCreate("default")
    artifacts.add("default", file("zpdk-release.aar"))
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")
    // Gson
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.github.chthai64:SwipeRevealLayout:1.4.0")
    //thu viện load image
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
    //bo goc tron cho ImageView
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.github.smarteist:autoimageslider:1.4.0")
    //SmsConfirmationView
    implementation ("com.github.fraggjkee:sms-confirmation-view:1.7.1")
    // Thu vien bieu do thong ke
    implementation ("com.github.AnyChart:AnyChart-Android:1.1.5")
    //thu vien export file
    implementation ("com.android.support:multidex:1.0.3")
    implementation ("org.apache.poi:poi:3.17")
    //zalopay
    implementation("com.squareup.okhttp3:okhttp:4.6.0")
    implementation("commons-codec:commons-codec:1.14")
    implementation(fileTree("D:\\android-studio\\Doan_nhom_6\\zalopay") {
        include("*.aar", "*.jar")
    })
}