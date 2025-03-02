rootProject.name = "N26BitcoinWatch"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")

include(":core:common")
include(":core:domain")

include(":core-data:common-data")
include(":core-data:data")
include(":core-data:cache")
include(":core-data:network")

include(":core-ui:common-ui")
include(":core-ui:design-system")

include(":feature:home")
include(":feature:details")

// todo ktlint run
