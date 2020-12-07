[![JetBrains IntelliJ plugins](https://img.shields.io/jetbrains/plugin/d/15520?logo=Intellij%20IDEA&style=for-the-badge)](https://plugins.jetbrains.com/plugin/15520-fixkture)
[![JetBrains IntelliJ Plugins](https://img.shields.io/jetbrains/plugin/v/15520?label=PLUGIN&logo=IntelliJ%20IDEA&style=for-the-badge)](https://plugins.jetbrains.com/plugin/15520-fixkture)
[![CircleCI](https://img.shields.io/circleci/build/github/pelletier197/Fixkture?label=Circle%20CI&logo=circleci&style=for-the-badge)](https://app.circleci.com/pipelines/github/pelletier197/Fixkture)

# Fixkture
Fixkture is a test fixture generator written in Kotlin for both Java and Kotlin. It supports generating complex objects that may be long and exhausting to generate manually.

This Intellij extension can generate complexe objects from a given data class, by provividing an arbitrary value as its constructor's argument. You can use this to generate large objects easily, making the testing process much simpler! 

## Example
Given the sample data class
```kotlin
data class Example(
    val first: String,
    val second: List<String>
)
```

### Kotlin
The generated fixture will look like this in kotlin
```kotlin
val example: Example = Example(first = "first", second = listOf("second"))
```
The library will use standard library functions to achieve fixture generation is as less code as possible, white keeping the fixture extremely readable.

### Java
Here is be the output code for the exact same class in Java
```java
public static final Example example = new Example("first", List.of("second"));
```
Again, standard functions of Java 11 are used to generate the fixture elements.

## Usage
You can download the plugin from the [Plugin Marketplace](https://plugins.jetbrains.com/plugin/15520-fixkture) and follow the instructions detailed in the documentation of the plugin.

## Contribute
You can contribute to this project by opening your own PR or by creating issues to propose improvement ideas. 
