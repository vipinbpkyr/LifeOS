<#
.SYNOPSIS
    Scaffolds a new LifeOS clean-architecture feature module.
.EXAMPLE
    .\tools\scaffold_feature.ps1 -Name "habits"
#>
param(
    [Parameter(Mandatory=$true)]
    [string]$Name
)

$FeatureName = $Name.ToLower().Trim()
$PascalName = (Get-Culture).TextInfo.ToTitleCase($FeatureName)
$ModuleDir = "feature/$FeatureName"

Write-Host "Creating feature module: :feature:$FeatureName ($PascalName)..." -ForegroundColor Cyan

$dirs = @(
    "$ModuleDir/src/main/java/com/lifeos/feature/$FeatureName/domain/model",
    "$ModuleDir/src/main/java/com/lifeos/feature/$FeatureName/domain/repository",
    "$ModuleDir/src/main/java/com/lifeos/feature/$FeatureName/domain/usecase",
    "$ModuleDir/src/main/java/com/lifeos/feature/$FeatureName/data/repository",
    "$ModuleDir/src/main/java/com/lifeos/feature/$FeatureName/data/di",
    "$ModuleDir/src/main/java/com/lifeos/feature/$FeatureName/presentation",
    "$ModuleDir/src/test/java/com/lifeos/feature/$FeatureName"
)

foreach ($d in $dirs) {
    New-Item -ItemType Directory -Path $d -Force | Out-Null
}

# Create build.gradle.kts
$buildGradle = @"
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.lifeos.feature.$FeatureName"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))
    implementation(project(":core:database"))
    implementation(project(":core:ai"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
}
"@
Set-Content -Path "$ModuleDir/build.gradle.kts" -Value $buildGradle

Write-Host "Feature module :feature:$FeatureName scaffolded successfully!" -ForegroundColor Green
Write-Host "Remember to include ':feature:$FeatureName' in settings.gradle.kts if not already included." -ForegroundColor Yellow
