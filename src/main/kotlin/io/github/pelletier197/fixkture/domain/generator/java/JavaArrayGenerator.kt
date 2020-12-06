package io.github.pelletier197.fixkture.domain.generator.java

import com.intellij.psi.PsiArrayType
import io.github.pelletier197.fixkture.domain.*
import io.github.pelletier197.fixkture.domain.generator.LanguageCallbackInstantiationFieldBuilder
import io.github.pelletier197.fixkture.domain.generator.java.Utils.extractType

object JavaArrayGenerator {
    fun generateArray(): InstantiationFieldBuilder {
        return LanguageCallbackInstantiationFieldBuilder(
                java = { context -> "new ${generateArrayTypeString(context)}[] { ${generateArrayElement(context).asJavaFlatValue(context)} }" },
                kotlin = { context -> "arrayOf<${generateArrayTypeString(context)}>(${generateArrayElement(context).asKotlinFlatValue(context)})" }
        )
    }

    private fun generateArrayTypeString(context: FieldConstructionContext): String {
        val t = arrayOf<Byte>(1)
        val type = extractType(context)
        if (type !is PsiArrayType) return "Object"
        return type.componentType.canonicalText
    }

    private fun generateArrayElement(context: FieldConstructionContext): InstantiationFieldBuilder {
        val type = extractType(context) ?: return NullInstantiationField()
        if (type !is PsiArrayType) return NullInstantiationField()
        return createInstantiationField(
                context = context.asClassInstantiationStatementBuilderContext(TargetElement.of(type.componentType))
        )
    }
}