import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.3.0"
}

group = "io.github.wasabithumb"
version = "0.2.0"
description = "Packages runnable JARs into native executables (Gradle plugin)"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.wasabithumb:jarstrap:0.2.0")
}

java {
    val javaVersion = JavaVersion.toVersion(17)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        apiVersion = "1.9"
        languageVersion = "1.9"
    }
}

gradlePlugin {
    plugins {
        create("jarstrap") {
            id = "${group}.${rootProject.name}"
            implementationClass = "io.github.wasabithumb.jarstrap.plugin.JARStrapPlugin"
            displayName = "JARStrap"
            description = project.description
            website = "https://github.com/WasabiThumb/jarstrap-gradle-plugin"
            vcsUrl = "https://github.com/WasabiThumb/jarstrap-gradle-plugin"
            tags = listOf(
                "publishing", "jar", "exe", "elf", "desktop"
            )
        }
    }
}
