import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.composeMultiplatform)
	alias(libs.plugins.composeCompiler)
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
			baseName = "coreUi-common" // todo review if this is removable from other modules
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
			implementation(libs.kotlinx.coroutines)

			implementation(compose.runtime)
			implementation(compose.foundation)
			implementation(compose.material3)
			implementation(compose.ui)
			implementation(compose.components.resources)
			implementation(compose.components.uiToolingPreview)

			implementation(libs.koin.core)
			implementation(libs.koin.compose.viewmodel)
			implementation(libs.navigation.compose)

			implementation(projects.core.common)
		}
	}
}

android {
	namespace = "com.n26.ui.common"
	compileSdk = 35

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
}

dependencies {
	debugImplementation(libs.androidx.compose.ui.tooling)
}
