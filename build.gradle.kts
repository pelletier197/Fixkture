import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

plugins {
    val kotlinVersion = "1.4.31"
    val gradleIntellijVersion = "0.7.2"
    val ktlintVersion = "10.0.0"
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.intellij") version gradleIntellijVersion
    id("org.jlleitschuh.gradle.ktlint") version ktlintVersion
}

group = "io.github.pelletier197"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinJvmCompile> {
    kotlinOptions {
        languageVersion = "1.4"
        apiVersion = "1.4"
        freeCompilerArgs = freeCompilerArgs + listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

intellij {
    version = "2020.2.3"
    type = "IC"
    setPlugins("java", "Kotlin")
    updateSinceUntilBuild = false
    pluginName = "Fixkture"
}

tasks.withType<org.jetbrains.intellij.tasks.PublishTask> {
    setToken(System.getenv("PUBLISH_TOKEN"))
    setChannels("Stable")
}
