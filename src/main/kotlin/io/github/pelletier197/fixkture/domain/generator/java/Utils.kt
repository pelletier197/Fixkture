package io.github.pelletier197.fixkture.domain.generator.java

import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiType
import io.github.pelletier197.fixkture.domain.FieldConstructionContext

object Utils {
    fun extractType(context: FieldConstructionContext): PsiType? {
        return when (val element = context.targetElement.element) {
            is PsiParameter -> element.type
            is PsiType -> element
            else -> null
        }
    }
}
