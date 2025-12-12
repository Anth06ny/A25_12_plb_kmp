import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)

    //kotlinxSerialization : kotlin version
    kotlin("plugin.serialization") version "2.1.0"
    //plugin pour créer et injecter dans BuildConfig les clés de local.properties
    id("com.github.gmazzo.buildconfig") version "5.5.1"
}

// Read API key from local.properties
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            //Client de requêtes spécifique à Android
            implementation("io.ktor:ktor-client-okhttp:3.2.2")

            //Si besoin du context
            implementation("io.insert-koin:koin-android:4.1.+")
        }
        iosMain.dependencies {
            //Client de requêtes spécifique à iOS
            implementation("io.ktor:ktor-client-darwin:3.2.2")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // (les interfaces en gros)
            implementation("io.ktor:ktor-client-core:3.2.2")
            //Intégration avec la bibliothèque de serialisation, gestion des headers
            implementation("io.ktor:ktor-client-content-negotiation:3.2.2")
            //Serialisation JSON
            implementation("io.ktor:ktor-serialization-kotlinx-json:3.2.2")
            //Pour le logger
            implementation("io.ktor:ktor-client-logging:3.2.2")

            //pour les icônes avec org.jetbrains spécifique à KPMP
            implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")
            //Pour le ViewModel
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.9.+")

            //ImageLoader (identique)
            implementation("io.coil-kt.coil3:coil-network-ktor3:3.2.0")
            implementation("io.coil-kt.coil3:coil-compose:3.2.0")

            //Navigation avec org.jetbrains spécifique à KPMP
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.9.+")

            //Injection dépendance KOIN
            implementation("io.insert-koin:koin-compose:4.1.+")
            implementation("io.insert-koin:koin-compose-viewmodel:4.1.+")
            implementation("io.insert-koin:koin-compose-viewmodel-navigation:4.1.+")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)

            //Client de requêtes spécifique au bureau sur JVM donc même qu'Android
            implementation("io.ktor:ktor-client-okhttp:3.2.2")
        }

    }
}

//A ajouter à la racine
buildConfig {
    // Définit le nom de la classe générée
    className("BuildConfig")
    // Le package où la classe sera générée
    packageName("com.amonteiro.a25_12_plb_kmp")

    // Récupération sécurisée de la clé
    val weatherAPIKey = localProperties.getProperty("weather.api.key") ?: ""

    println("weatherAPIKey chargée : $weatherAPIKey")

    // Crée le champ pour tous les targets (Android, iOS, Desktop)
    buildConfigField("String", "WEATHER_API_KEY", "\"$weatherAPIKey\"")
}

android {
    namespace = "com.amonteiro.a25_12_plb_kmp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.amonteiro.a25_12_plb_kmp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.amonteiro.a25_12_plb_kmp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.amonteiro.a25_12_plb_kmp"
            packageVersion = "1.0.0"
        }
    }
}
