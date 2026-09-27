package com.lifeos.architecture

import com.lemonappdev.konsist.api.KoModifier
import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.jupiter.api.Test

class ArchitectureTest {

    @Test
    fun `domain layer must not depend on android framework`() {
        Konsist
            .scopeFromProject()
            .files
            .filter { it.hasPackage("..domain..") }
            .assertTrue { file ->
                file.imports.none { import ->
                    import.name.startsWith("android.") ||
                    (import.name.startsWith("androidx.") && !import.name.startsWith("androidx.annotation"))
                }
            }
    }

    @Test
    fun `view models must reside in presentation package and have ViewModel suffix and inherit ViewModel`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withNameEndingWith("ViewModel")
            .assertTrue { clazz ->
                clazz.resideInPackage("..presentation..") &&
                clazz.parents().any { it.name == "ViewModel" }
            }
    }

    @Test
    fun `use cases must reside in domain usecase package and have single invoke method`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withNameEndingWith("UseCase")
            .assertTrue { clazz ->
                clazz.resideInPackage("..domain.usecase..") &&
                clazz.functions().count { it.name == "invoke" && it.hasModifier(KoModifier.OPERATOR) } == 1 &&
                clazz.functions().count { it.hasPublicOrDefaultModifier } == 1
            }
    }

    @Test
    fun `repository interfaces must reside in domain repository package`() {
        Konsist
            .scopeFromProject()
            .interfaces()
            .withNameEndingWith("Repository")
            .assertTrue { iface ->
                iface.resideInPackage("..domain.repository..")
            }
    }

    @Test
    fun `repository implementations must reside in data repository package`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withNameEndingWith("RepositoryImpl")
            .assertTrue { clazz ->
                clazz.resideInPackage("..data.repository..")
            }
    }

    @Test
    fun `every compose screen file must contain at least one preview composable`() {
        Konsist
            .scopeFromProject()
            .files
            .filter { (it.name.endsWith("Screen") || it.nameWithExtension.endsWith("Screen.kt")) && !it.path.contains("build") }
            .assertTrue { file ->
                file.functions().any { function ->
                    function.hasAnnotation { annotation ->
                        annotation.name == "Preview"
                    }
                }
            }
    }

    @Test
    fun `navigation routes must not contain custom serializable types and only allow primitive types`() {
        val allowedTypes = setOf("String", "Int", "Long", "Boolean", "Float", "Double")
        Konsist
            .scopeFromProject()
            .classes()
            .filter { clazz -> clazz.parents().any { it.name == "LifeOsDestination" } }
            .assertTrue { clazz ->
                clazz.properties().all { property ->
                    val cleanTypeName = property.type?.name?.removeSuffix("?")
                    cleanTypeName in allowedTypes
                }
            }
    }
}
