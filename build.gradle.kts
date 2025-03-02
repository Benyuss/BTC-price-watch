buildscript {
	repositories {
		mavenCentral()
		google()
		gradlePluginPortal()
	}
	dependencies {
		classpath(libs.buildKonfig.plugin)
	}
}

plugins {
	alias(libs.plugins.androidApplication) apply false
	alias(libs.plugins.androidLibrary) apply false
	alias(libs.plugins.composeCompiler) apply false
	alias(libs.plugins.composeMultiplatform) apply false
	alias(libs.plugins.kotlinMultiplatform) apply false
	alias(libs.plugins.kotlinxSerialization) apply false
	alias(libs.plugins.buildKonfig) apply false
	alias(libs.plugins.kotest) apply false
	alias(libs.plugins.spotless) apply false
}

subprojects {
	repositories {
		mavenCentral()
		google()
		gradlePluginPortal()
	}

	apply(plugin = rootProject.libs.plugins.spotless.get().pluginId)

	tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
		kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
		kotlinOptions.freeCompilerArgs +=
			listOf(
				"-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
				"-opt-in=kotlin.time.ExperimentalTime",
			)
	}

	extensions.configure<com.diffplug.gradle.spotless.SpotlessExtension> {
		kotlin {
			target("**/*.kt")
			targetExclude("${layout.buildDirectory}/**/*.kt")
			targetExclude("**/generated/**")
			ktlint("1.5.0")
				.customRuleSets(
					listOf(
						"io.nlopez.compose.rules:ktlint:0.4.16",
					),
				)
		}
		format("kts") {
			target("**/*.kts")
			targetExclude("${layout.buildDirectory}/**/*.kts")
			targetExclude("**/generated/**")
		}
		format("xml") {
			target("**/*.xml")
			targetExclude("**/build/**/*.xml")
		}
	}
}
