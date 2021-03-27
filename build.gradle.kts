import org.jetbrains.kotlin.gradle.dsl.KotlinCommonOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
    val kotlinVersion = "1.4.21"
    val gradleIntellijVersion = "0.7.2"
    val ktlintVersion = "10.0.0"
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.intellij") version gradleIntellijVersion
    id("org.jlleitschuh.gradle.ktlint") version ktlintVersion
    id("java")
}

group = "io.github.pelletier197"

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile<KotlinCommonOptions>> {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf("-Xjsr305=strict")
    }
}

//
// java {
//
// }
// sourceCompatibility = JavaVersion.11
// targetCompatibility = '11'
//
// compileKotlin {
//     kotlinOptions {
//         jvmTarget = '11'
//     }
// }
//
// compileTestKotlin {
//     kotlinOptions {
//         jvmTarget = '11'
//     }
// }
//
// intellij {
//     version = "2020.2.3"
//     type = "IC"
//     plugins = ["java", "Kotlin"]
//     updateSinceUntilBuild = false
// }
//
// publishPlugin {
//     token =  System.getenv("PUBLISH_TOKEN")
//     channels = "Stable"
// }
