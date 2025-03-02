import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.androidApplication)
	alias(libs.plugins.composeMultiplatform)
	alias(libs.plugins.composeCompiler)
	alias(libs.plugins.kotlinxSerialization)
}

kotlin {
	androidTarget {
		@OptIn(ExperimentalKotlinGradlePluginApi::class)
		compilerOptions {
			jvmTarget.set(JvmTarget.JVM_17)
		}
	}

	listOf(
		iosX64(),
		iosArm64(),
		iosSimulatorArm64(),
	).forEach { iosTarget ->
		iosTarget.binaries.framework {
			baseName = "CoinTrackerApp"
			isStatic = true
		}
	}

	sourceSets {
		androidMain.dependencies {
			implementation(libs.androidx.compose.ui.tooling.preview)
			implementation(libs.androidx.activity.compose)
		}
		commonMain.dependencies {
			implementation(libs.kotlinx.datetime)

			implementation(compose.runtime)
			implementation(compose.foundation)
			implementation(compose.material3)
			implementation(compose.ui)
			implementation(compose.components.resources)
			implementation(compose.components.uiToolingPreview)

			implementation(libs.ktor.client.core)
			implementation(libs.ktor.client.content.negotiation)
			implementation(libs.ktor.serialization.kotlinx.json)

			implementation(libs.koin.core)
			implementation(libs.koin.compose.viewmodel)
			implementation(libs.navigation.compose)

			implementation(projects.core.common)
			implementation(projects.core.domain)
			implementation(projects.coreData.data)
			implementation(projects.coreUi.commonUi)
			implementation(projects.coreUi.designSystem)

			implementation(projects.feature.home)
			implementation(projects.feature.details)
		}
	}
}

android {
	namespace = "com.n26.cointracker"
	compileSdk = 35

	defaultConfig {
		applicationId = "com.n26.cointracker"
		minSdk = 26
		targetSdk = 35
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
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
}

dependencies {
	debugImplementation(libs.androidx.compose.ui.tooling)
}
