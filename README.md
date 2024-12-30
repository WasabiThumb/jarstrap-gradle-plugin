# jarstrap-gradle-plugin
A Gradle plugin that uses [JARStrap](https://github.com/WasabiThumb/jarstrap) to
wrap a project's existing runnable JAR into a native executable. This plugin exposes
all features of **JARStrap** while using the build environment to provide sensible
default values.

## Example
```kotlin
import io.github.wasabithumb.jarstrap.plugin.extensions.*

plugins {
    id("java")
    id("io.github.wasabithumb.jarstrap-gradle-plugin") version "0.2.0"
}

jarstrap {
    /* The task to receive the runnable JAR from */
    source = tasks.jar

    /* Strip debug symbols */
    release = true

    /* Build for 64-bit */
    target("amd64") {
        x64 = true
    }

    /* Build for 32-bit */
    target("i686") {
        x64 = false
    }
}
```
- Groovy DSL will not be officially supported