import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlinxSerialization)
}

kotlin {
	androidTarget {
		compilerOptions {
			jvmTarget.set(JvmTarget.JVM_11)
		}
	}

	listOf(
		iosX64(),
		iosArm64(),
		iosSimulatorArm64(),
	).forEach { iosTarget ->
		iosTarget.binaries.framework {
			baseName = "coreData-data"
			isStatic = true
		}
	}

	sourceSets {
		commonMain.dependencies {
			implementation(projects.core.common)
			implementation(projects.core.domain)
			implementation(projects.coreData.commonData)
			implementation(projects.coreData.cache)
			implementation(projects.coreData.network)

			implementation(libs.ktor.client.core)
			implementation(libs.ktor.client.logging)
			implementation(libs.ktor.client.content.negotiation)
			implementation(libs.ktor.serialization.kotlinx.json)

			implementation(libs.koin.core)
			implementation(libs.kotlinx.datetime)
			implementation(libs.kotlinx.coroutines)
		}
	}
}

android {
	namespace = "com.n26.cointracker.data.data"
	compileSdk = 35

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
}
