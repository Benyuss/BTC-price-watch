import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlinxSerialization)
	alias(libs.plugins.kotest)
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
			baseName = "coreData-cache"
			isStatic = true
		}
	}

	sourceSets {
		androidUnitTest.dependencies {
			implementation(libs.kotest.framework.engine)
			implementation(libs.kotest.assertions.core)
			implementation(libs.kotest.junit)

			implementation(libs.turbine)
			implementation(kotlin("test"))
		}

		commonMain.dependencies {
			implementation(projects.core.common)
			implementation(projects.core.domain)

			implementation(libs.koin.core)
			implementation(libs.kotlinx.datetime)
			implementation(libs.kotlinx.coroutines)
		}
	}
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()
}

android {
	namespace = "com.n26.cointracker.data.cache"
	compileSdk = 35
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
}
