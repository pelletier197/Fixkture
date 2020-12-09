[![JetBrains IntelliJ plugins](https://img.shields.io/jetbrains/plugin/d/15520?logo=Intellij%20IDEA&style=for-the-badge)](https://plugins.jetbrains.com/plugin/15520-fixkture)
[![JetBrains IntelliJ Plugins](https://img.shields.io/jetbrains/plugin/v/15520?label=PLUGIN&logo=IntelliJ%20IDEA&style=for-the-badge)](https://plugins.jetbrains.com/plugin/15520-fixkture)
[![CircleCI](https://img.shields.io/circleci/build/github/pelletier197/Fixkture?label=Circle%20CI&logo=circleci&style=for-the-badge)](https://app.circleci.com/pipelines/github/pelletier197/Fixkture)

<p align="center">
  <img src="./logo/logo.png">
</p>

# Fixkture
Fixkture is an Intellij Plugin to generate test fixtures that supports both Java and Kotlin. It helps generating complex objects from a target class, which may be long and exhausting to do manually, especially for big objects. It does so by providing arbitrary parameters in the object's constructor.

## Example
Given the sample data class
```kotlin
data class Example(
    val first: String,
    val second: List<String>
)
```
> This is a really simple class, but the plugin will easily support generating an extremely complex object with nested objects in it.

### Kotlin
The generated fixture will look like this
```kotlin
val example: Example = Example(first = "first", second = listOf("second"))
```
The library will use standard library functions to achieve fixture generation in as less code as possible, white keeping the fixture extremely readable.

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
