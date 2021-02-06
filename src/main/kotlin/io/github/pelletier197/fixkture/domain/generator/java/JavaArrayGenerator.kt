package io.github.pelletier197.fixkture.domain.generator.java

import com.intellij.psi.PsiArrayType
import com.intellij.psi.PsiPrimitiveType
import io.github.pelletier197.fixkture.domain.FieldConstructionContext
import io.github.pelletier197.fixkture.domain.InstantiationFieldBuilder
import io.github.pelletier197.fixkture.domain.NullInstantiationField
import io.github.pelletier197.fixkture.domain.TargetElement
import io.github.pelletier197.fixkture.domain.createInstantiationField
import io.github.pelletier197.fixkture.domain.generator.LanguageCallbackInstantiationFieldBuilder
import io.github.pelletier197.fixkture.domain.generator.NestedElementInstantiationFieldBuilder
import io.github.pelletier197.fixkture.domain.generator.java.Utils.extractType

object JavaArrayGenerator {
    fun generateArray(): InstantiationFieldBuilder {
        return LanguageCallbackInstantiationFieldBuilder(
            java = { context ->
                "new ${generateJavaArrayTypeString(context)}[] { ${
                generateArrayElement(context).asJavaFlatValue(
                    context
                )
                } }"
            },
            kotlin = { context ->
                "${selectKotlinArrayGeneratorFunction(context)}(${
                generateArrayElement(context).asKotlinFlatValue(
                    context
                )
                })"
            }
        )
    }

    private fun generateJavaArrayTypeString(context: FieldConstructionContext): String {
        val type = extractType(context)
        if (type !is PsiArrayType) return "Object"
        return type.componentType.canonicalText
    }

    private fun selectKotlinArrayGeneratorFunction(context: FieldConstructionContext): String {
        val type = extractType(context)
        if (type !is PsiArrayType) return "Object"
        return when (val componentType = type.componentType) {
            is PsiPrimitiveType -> when (componentType.name) {
                "int" -> "intArrayOf"
                "float" -> "floatArrayOf"
                "double" -> "doubleArrayOf"
                "long" -> "longArrayOf"
                "char" -> "charArrayOf"
                "short" -> "shortArrayOf"
                "boolean" -> "booleanArrayOf"
                "byte" -> "byteArrayOf"
                else -> "arrayOf"
            }
            else -> "arrayOf"
        }
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
