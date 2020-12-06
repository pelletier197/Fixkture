package io.github.pelletier197.fixkture.domain.generator.java

import com.intellij.psi.PsiArrayType
import io.github.pelletier197.fixkture.domain.*
import io.github.pelletier197.fixkture.domain.generator.LanguageCallbackInstantiationFieldBuilder
import io.github.pelletier197.fixkture.domain.generator.NestedElementInstantiationFieldBuilder
import io.github.pelletier197.fixkture.domain.generator.java.Utils.extractType

object JavaArrayGenerator {
    fun generateArray(): InstantiationFieldBuilder {
        return LanguageCallbackInstantiationFieldBuilder(
                java = { context -> "new ${generateJavaArrayTypeString(context)}[] { ${generateArrayElement(context).asJavaFlatValue(context)} }" },
                kotlin = { context -> "arrayOf<${generateKotlinArrayTypeString(context)}>(${generateArrayElement(context).asKotlinFlatValue(context)})" }
        )
    }

    private fun generateJavaArrayTypeString(context: FieldConstructionContext): String {
        val type = extractType(context)
        if (type !is PsiArrayType) return "Object"
        return type.componentType.canonicalText
    }

    private fun generateKotlinArrayTypeString(context: FieldConstructionContext): String {
        val javaString = generateJavaArrayTypeString(context)
        return convertJavaArrayTypeStringToKotlin(javaString)
    }

    // Converts Object[] to Array<Object>
    private fun convertJavaArrayTypeStringToKotlin(javaString: String): String {
        val index = javaString.lastIndexOf("[]")
        if (index < 0) return javaString
        return convertJavaArrayTypeStringToKotlin("Array<${javaString.removeRange(index, index + 2)}>")
    }

    private fun generateArrayElement(context: FieldConstructionContext): InstantiationFieldBuilder {
        val type = extractType(context) ?: return NullInstantiationField()
        if (type !is PsiArrayType) return NullInstantiationField()
        return NestedElementInstantiationFieldBuilder(
                elementBuilder = createInstantiationField(
                        context = context.asClassInstantiationStatementBuilderContext(TargetElement.of(type.componentType))
                ),
                targetElement = TargetElement.of(type.componentType)
        )
    }
}