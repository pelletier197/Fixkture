package io.github.pelletier197.fixkture.domain

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiType
import com.intellij.psi.util.PsiUtil

interface TargetElement {
    val name: String
    val element: Any

    companion object {
        fun of(element: PsiElement): TargetElement {
            return PsiElementTargetElement(element)
        }

        fun of(type: PsiType): TargetElement {
            return PsiTypeTargetElement(type)
        }
    }
}

data class PsiElementTargetElement(
    val psiElement: PsiElement
) : TargetElement {
    override val name: String
        get() = PsiUtil.getName(psiElement) ?: "fixture"
    override val element: Any
        get() = psiElement
}

data class PsiTypeTargetElement(
    val type: PsiType
) : TargetElement {
    override val name: String
        get() = PsiUtil.resolveClassInType(type)?.let { PsiUtil.getName(it) } ?: "fixture"
    override val element: Any
        get() = type
}
