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
            .scopeFromProduction()
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
            .scopeFromProduction()
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
            .scopeFromProduction()
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
            .scopeFromProduction()
            .interfaces()
            .withNameEndingWith("Repository")
            .assertTrue { iface ->
                iface.resideInPackage("..domain.repository..")
            }
    }

    @Test
    fun `repository implementations must reside in data repository package`() {
        Konsist
            .scopeFromProduction()
            .classes()
            .withNameEndingWith("RepositoryImpl")
            .assertTrue { clazz ->
                clazz.resideInPackage("..data.repository..")
            }
    }
}
