import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlinxSerialization)
	alias(libs.plugins.buildKonfig)
	alias(libs.plugins.kotest)
}

kotlin {
	androidTarget {
		@OptIn(ExperimentalKotlinGradlePluginApi::class)
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
			baseName = "coreData-network"
			isStatic = true
		}
	}

	sourceSets {
		androidUnitTest.dependencies {
			implementation(libs.kotest.framework.engine)
			implementation(libs.kotest.assertions.core)
			implementation(libs.kotest.junit)

			implementation(libs.kotlin.coroutines.test)
			implementation(libs.mockk)

			implementation(libs.ktor.client.mock)

			implementation(libs.turbine)
			implementation(kotlin("test"))
		}

		iosMain.dependencies {
			implementation(libs.ktor.client.darwin)
		}

		androidMain.dependencies {
			implementation(libs.ktor.client.okhttp)
		}

		commonMain.dependencies {
			implementation(projects.core.common)
			implementation(projects.core.domain)
			implementation(projects.coreData.commonData)

			implementation(libs.ktor.client.core)
			implementation(libs.ktor.client.logging)
			implementation(libs.ktor.client.content.negotiation)
			implementation(libs.ktor.serialization.kotlinx.json)

			implementation(libs.koin.core)
			implementation(libs.kotlinx.datetime)
			implementation(libs.kotlinx.coroutines)

			implementation(libs.kermit)
		}
	}
}

android {
	namespace = "com.n26.cointracker.data.network"
	compileSdk = 35
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()
}

buildkonfig {
	packageName = "com.n26.cointracker.data.network"

	val coinGeckoApiUrl = "coinGeckoApiUrl"

	defaultConfigs {
		buildConfigField(
			type = FieldSpec.Type.STRING,
			name = coinGeckoApiUrl,
			value = "https://api.coingecko.com/api/v3/",
		)
	}
}
