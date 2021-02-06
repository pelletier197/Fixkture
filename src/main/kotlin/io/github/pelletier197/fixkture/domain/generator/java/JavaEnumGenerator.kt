package io.github.pelletier197.fixkture.domain.generator.java

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiType
import com.intellij.psi.util.PsiUtil
import io.github.pelletier197.fixkture.domain.FieldConstructionContext
import io.github.pelletier197.fixkture.domain.InstantiationFieldBuilder
import io.github.pelletier197.fixkture.domain.generator.LanguageCallbackInstantiationFieldBuilder
import org.jetbrains.kotlin.asJava.classes.KtUltraLightClass

object JavaEnumGenerator {
    fun generateEnum(): InstantiationFieldBuilder {
        return LanguageCallbackInstantiationFieldBuilder(
            java = { context -> generateJava(context) },
            kotlin = { context -> generateKotlin(context) }
        )
    }

    private fun generateJava(context: FieldConstructionContext): String {
        val element = extractClass(context)
        if (element is PsiClass) {
            val enumValue = element.fields.asList().ifEmpty { return "null" }
            return "${element.qualifiedName}.${enumValue.random().name}"
        }
        return "null"
    }

    private fun generateKotlin(context: FieldConstructionContext): String {
        val element = extractClass(context)
        if (element is KtUltraLightClass) {
            val enumValues = element.fields.asList().ifEmpty { return "null" }
            return "${element.qualifiedName}.${enumValues.random().name}"
        }
        return "null"
    }

    private fun extractClass(context: FieldConstructionContext): Any {
        val element = context.targetElement.element
        if (element is PsiType) return PsiUtil.resolveClassInType(element) ?: element
        return element
    }
}
