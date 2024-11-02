import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink
import org.jetbrains.kotlin.org.apache.commons.io.FileUtils
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.androidxRoom)
    alias(libs.plugins.buildconfig)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotestMultiplatform)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
}

buildConfig {
    forClass("Secret") {
        packageName("secret")
        val secretProperties =
            Properties().apply {
                load(project.rootProject.file("secret.properties").reader())
            }
        buildConfigField("GoogleBookApiKey", secretProperties.getProperty("google.book.api.key"))
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

compose {
    resources {
        packageOfResClass = "io.kort.inbooks.ui.resource"
    }
}

composeCompiler {
    featureFlags = setOf(
        ComposeFeatureFlag.StrongSkipping,
        ComposeFeatureFlag.OptimizeNonSkippingGroups,
        ComposeFeatureFlag.IntrinsicRemember,
    )
}

kotlin {
    androidTarget {
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        // Common compiler options applied to all Kotlin source sets
        // Workaround for Room.
        freeCompilerArgs.add("-Xexpect-actual-classes")
        optIn.add("kotlin.uuid.ExperimentalUuidApi")
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            linkerOpts.add("-lsqlite3") // add sqlite
        }
    }

    dependencies {
        add("kspAndroid", libs.androidx.room.compiler)
        add("kspIosSimulatorArm64", libs.androidx.room.compiler)
        add("kspIosX64", libs.androidx.room.compiler)
        add("kspIosArm64", libs.androidx.room.compiler)
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":common"))
            implementation(compose.components.uiToolingPreview)
            implementation(compose.components.resources)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(libs.androidx.annotation)
            implementation(libs.androidx.datastore.preference)
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.coil)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)
            implementation(libs.jetbrain.androidx.lifecycle.common)
            implementation(libs.jetbrain.androidx.compose.material.navigation)
            implementation(libs.jetbrain.androidx.navigation.compose)
            implementation(libs.kamel)
            implementation(libs.kermit)
            implementation(libs.kermit.koin)
            implementation(libs.kmpalette.core)
            implementation(libs.kmpalette.extensions.network)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.compose)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
            implementation(project.dependencies.platform(libs.ktor.bom))
            implementation(libs.bundles.ktor.client)
            implementation(libs.richeditor)
            implementation(libs.uuid)
        }

        commonTest.dependencies {
            implementation(libs.androidx.room.testing)
            implementation(libs.kotest.assertions.core)
            implementation(libs.kotest.framework.engine)
            implementation(libs.kotlin.test)
            implementation(libs.ktor.client.mock)
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.annotation.jvm)
            implementation(libs.androidx.core.splashscreen)
            implementation(compose.preview)
            implementation(libs.koin.android)
            implementation(libs.ktor.client.android)
        }

        androidInstrumentedTest.dependencies {
            implementation(libs.androidx.test.core)
            implementation(libs.androidx.room.testing)
            implementation(libs.androidx.junit.ktx)
            implementation(libs.ktor.client.mock.jvm)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "io.kort.inbooks"
    compileSdk =
        libs.versions.android.compileSdk
            .get()
            .toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "io.kort.inbooks"
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.android.targetSdk
                .get()
                .toInt()
        versionCode = 8
        versionName = "0.1.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }

        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    sourceSets {
        getByName("androidTest").assets.srcDir("$projectDir/schemas")
    }

    dependencies {
        debugImplementation(compose.uiTooling)
    }
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }
}

// 把 schemas 複製到 ios test 拿得到的地方
tasks.withType(KotlinNativeLink::class.java).forEach { task ->
    if (task.name.contains("linkDebugTestIos")) {
        val inputSchemaDir = layout.projectDirectory.dir("schemas").asFile
        val outputSchemaDir = File(task.destinationDirectory.asFile.get(), "/schemas")
        task.doLast {
            FileUtils.copyDirectory(inputSchemaDir, outputSchemaDir)
        }
    }
}