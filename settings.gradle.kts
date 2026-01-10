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
    }
}

rootProject.name = "Drank"
include(":app")
include(":domain:drinks")
include(":data:drinks")
include(":data:drinks:local")
include(":feature:home")
include(":common:ui")
include(":feature:base")
include(":feature:location")
include(":domain:location")
include(":data:location")
include(":data:location:local")
include(":data:location:remote")
include(":feature:consume")
