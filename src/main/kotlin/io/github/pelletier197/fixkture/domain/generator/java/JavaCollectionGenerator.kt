package io.github.pelletier197.fixkture.domain.generator.java

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiParameter
import io.github.pelletier197.fixkture.domain.FieldConstructionContext
import io.github.pelletier197.fixkture.domain.InstantiationFieldBuilder
import io.github.pelletier197.fixkture.domain.generator.CallbackClassInstantiationFieldBuilder
import io.github.pelletier197.fixkture.domain.generator.LanguageCallbackValueGenerator
import io.github.pelletier197.fixkture.domain.generator.NullInstantiationField

private data class CollectionElementInstantiationFieldBuilder(
        private val elementBuilder: InstantiationFieldBuilder,
        private val elementClass: PsiClass,
) : InstantiationFieldBuilder {
    override fun asJavaConstructorArgument(context: FieldConstructionContext): String {
        return elementBuilder.asJavaConstructorArgument(context.copy(targetElement = elementClass))
    }

    override fun asKotlinConstructorArgument(context: FieldConstructionContext): String {
        return elementBuilder.asKotlinConstructorArgument(context.copy(targetElement = elementClass))
    }

    override fun asJavaFlatValue(context: FieldConstructionContext): String {
        return elementBuilder.asJavaFlatValue(context.copy(targetElement = elementClass))
    }

    override fun asKotlinFlatValue(context: FieldConstructionContext): String {
        return elementBuilder.asKotlinFlatValue(context.copy(targetElement = elementClass))
    }

}

object JavaCollectionGenerator {

    fun generateList(): InstantiationFieldBuilder {
        return CallbackClassInstantiationFieldBuilder(
                LanguageCallbackValueGenerator(
                        java = { context -> "java.util.List.of(${createCollectionArgument(listClass = context.targetElement).asJavaFlatValue(context)})" },
                        kotlin = { context -> "listOf(${createCollectionArgument(listClass = context.targetElement).asKotlinFlatValue(context)})" }
                )
        )
    }

    fun generateIterable(): InstantiationFieldBuilder {
        return generateList()
    }

    private fun createCollectionArgument(listClass: PsiElement): InstantiationFieldBuilder {
        if(listClass is PsiParameter) {
            val type = listClass.type.
            val str = type.toString()
            println(type)
        }
        return NullInstantiationField()
    }
}