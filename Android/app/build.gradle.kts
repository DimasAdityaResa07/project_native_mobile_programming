plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.project_uts_uas_nmp_sem6"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.project_uts_uas_nmp_sem6"
        minSdk = 28
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
    sourceSets {
        getByName("main") {
            res {
                srcDirs("src/main/res/layouts/crud_kendaraan_admin",
                    "src/main/res/layouts/crud_penerimaan_laporan_admin",
                    "src/main/res/layouts/crud_kategori_admin",
                    "src/main/res/layouts/crud_penanganan_laporan_admin",
                    "src/main/res/layouts/crud_pelapor_admin",
                    "src/main/res/layouts/crud_petugas_admin",
                    "src/main/res/layouts/tampilan_admin",
                    "src/main/res/layouts/tampilan_user",
                    "src/main/res/layouts/awal_allrole",
                    "src/main/res/layouts/",
                    "src/main/res")
            }
        }
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
    implementation ("com.github.clans:fab:1.6.4")
    implementation ("com.android.volley:volley:1.2.1")
}