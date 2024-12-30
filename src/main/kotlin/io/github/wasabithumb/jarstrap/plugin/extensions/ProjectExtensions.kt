package io.github.wasabithumb.jarstrap.plugin.extensions

import io.github.wasabithumb.jarstrap.plugin.JARStrapTask
import org.gradle.api.Action
import org.gradle.api.Project

/**
 * Configures the jarstrap plugin task.
 * No executables will be produced until at least one target is created.
 */
fun Project.jarstrap(configure: Action<JARStrapTask>) {
    configure.execute(this.tasks.getByName("jarstrap") as JARStrapTask)
}