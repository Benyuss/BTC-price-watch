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
			baseName = "coreData-common"
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

			implementation(libs.kermit)

			implementation(libs.ktor.client.core)
			implementation(libs.ktor.client.logging)
			implementation(libs.ktor.client.content.negotiation)
			implementation(libs.ktor.serialization.kotlinx.json)

			implementation(libs.koin.core)
			implementation(libs.kotlinx.datetime)
		}
	}
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()
}

android {
	namespace = "com.n26.data.common"
	compileSdk = 35
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
}
