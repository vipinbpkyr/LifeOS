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

rootProject.name = "LifeOS"

// Main Application
include(":app")

// Core Modules
include(":core:common")
include(":core:model")
include(":core:database")
include(":core:network")
include(":core:ai")
include(":core:designsystem")
include(":core:navigation")

// Feature Modules
include(":feature:dashboard")
include(":feature:reminders")
include(":feature:tradingjournal")
include(":feature:learning")
include(":feature:travel")
include(":feature:assistant")

// Architecture & Quality Guardrails
include(":architecture-tests")
