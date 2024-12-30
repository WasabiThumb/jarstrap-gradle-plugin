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


## Manifest Utilities
An additional feature that this plugin has over base **JARStrap** is the ability to modify manifest entries at the
global & target level, for instance:
```kotlin
jarstrap {
    // ...
    manifest {
        put("Main-Class", "path.to.your.MainClass")
    }
    target("foo") {
        // ...
        manifest {
            this["Target-ID"] = "foo"
            remove("Unwanted-Key")
        }
    }
    target("bar") {
        // ...
        manifest {
            this += "Target-ID" to "bar"
            this -= "Unwanted-Key"
        }
    }
}
```

## License
```text
Copyright 2024 Wasabi Codes

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
