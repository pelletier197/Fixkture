package io.github.pelletier197.fixkture.api.generators

import com.intellij.codeInsight.generation.ClassMember
import com.intellij.codeInsight.generation.PsiMethodMember
import com.intellij.ide.util.MemberChooserBuilder
import com.intellij.ide.util.TreeClassChooserFactory
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.codeStyle.JavaCodeStyleManager
import com.intellij.psi.codeStyle.VariableKind
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTypesUtil

fun selectTargetTargetClass(project: Project): PsiClass? {
    val classChooserFactory = TreeClassChooserFactory.getInstance(project)
    val classChooser = classChooserFactory.createAllProjectScopeChooser("Target class")
    classChooser.showDialog()
    return classChooser.selected
}

fun selectTargetConstructor(psiClass: PsiClass, project: Project): PsiMethod? {
    val constructors = psiClass.constructors

    if (constructors.size > 1) {
        return selectConstructorInList(constructors, project)
    }

    return constructors.getOrNull(0)
}

fun selectInterfaceImplementationClass(interfaceClass: PsiClass, project: Project): PsiClass? {
    val dialog = TreeClassChooserFactory.getInstance(project).createInheritanceClassChooser(
        "Target class for interface `${interfaceClass.name}`",
        GlobalSearchScope.allScope(project),
        interfaceClass,
        null
    )
    dialog.showDialog()
    return dialog.selected
}

fun suggestVariableName(targetType: PsiClass, project: Project): String {
    val codeStyleManager = JavaCodeStyleManager.getInstance(project)
    return codeStyleManager.suggestVariableName(VariableKind.LOCAL_VARIABLE, null, null, PsiTypesUtil.getClassType(targetType)).names.firstOrNull() ?: "fixture"
}

fun selectConstructorInList(constructors: Array<PsiMethod>, project: Project): PsiMethod? {
    val constructorChooser = MemberChooserBuilder<ClassMember>(project)
        .also { it.allowEmptySelection(false) }
        .also { it.allowMultiSelection(false) }
        .also { it.setTitle("Choose constructor") }
        .createBuilder(constructors.map { PsiMethodMember(it) }.toTypedArray())
    constructorChooser.show()
    return (constructorChooser.selectedElements?.getOrNull(0) as PsiMethodMember?)?.element
}
