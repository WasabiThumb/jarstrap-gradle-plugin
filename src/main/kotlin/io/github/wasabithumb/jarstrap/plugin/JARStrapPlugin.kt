package io.github.wasabithumb.jarstrap.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType

class JARStrapPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val sources = target.tasks.withType(Jar::class)
        var initialSource: Jar? = null
        var weight = 0

        sources.forEach {
            val w: Int = when (it.name) {
                "shadowJar" -> 3
                "jar" -> 2
                else -> 1
            }
            if (w > weight) {
                initialSource = it
                weight = w
            }
        }

        if (initialSource == null) {
            throw AssertionError("Project has no Jar tasks")
        }

        val provider = target.tasks.register<JARStrapTask>("jarstrap") {
            resolvedSource = initialSource!!
            outputDirectory.set(this.project.layout.buildDirectory.dir("jarstrap"))
        }

        target.tasks.named("build") {
            dependsOn(provider)
        }
    }

}
