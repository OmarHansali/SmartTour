import java.util.Properties

// Load local.properties
val localProperties = Properties()
val localPropertiesFile = file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Mapbox repository disabled - using alternate mapping solutions
        /*maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {
                create<org.gradle.authentication.http.BasicAuthentication>("basic")
            }
            credentials {
                username = "mapbox"
                password = localProperties["MAPBOX_DOWNLOADS_TOKEN"] as String? ?: ""
            }
        }*/
    }
}

rootProject.name = "SmartTour"
include(":app")