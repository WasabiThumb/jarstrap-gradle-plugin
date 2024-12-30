package io.github.wasabithumb.jarstrap.plugin

import io.github.wasabithumb.jarstrap.JARStrap
import io.github.wasabithumb.jarstrap.packager.Packager
import io.github.wasabithumb.jarstrap.plugin.config.DeferringPackagerConfiguration
import io.github.wasabithumb.jarstrap.plugin.config.PackagerConfiguration
import io.github.wasabithumb.jarstrap.plugin.target.PackagerTarget
import io.github.wasabithumb.jarstrap.plugin.util.GradleLoggerHandler
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.file.RegularFile
import org.gradle.api.internal.provider.Providers
import org.gradle.api.logging.LogLevel
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.bundling.Jar
import java.io.File
import java.util.*

abstract class JARStrapTask : DefaultTask(), DeferringPackagerConfiguration {

    private val targets: MutableMap<String, PackagerTarget> = mutableMapOf()
    private lateinit var _source: Jar

    /**
     * The task which produces the JAR to wrap into an executable.
     * The default task is determined in priority order:
     * 1. Any task named "shadowJar" extending Jar
     * 2. Any task named "jar" extending Jar
     * 3. Any task extending Jar
     */
    @get:Internal
    var resolvedSource: Jar
        get() = this._source
        set(value) {
            this.setDependsOn(listOf(value))
            this._source = value
        }

    /**
     * @see resolvedSource
     */
    @get:Internal
    var source: Provider<out Jar>
        get() = Providers.of(this._source)
        set(value) {
            this.resolvedSource = value.get()
        }

    @Internal
    override val config: PackagerConfiguration = PackagerConfiguration.create()

    /**
     * The rule to use for picking the primary artifact out of the potentially multiple artifacts produced by
     * the source task. The default rule asserts that there is exactly 1 output, and provides that output.
     * @see FileCollection.getSingleFile
     */
    @get:Input
    var fileSelector: (FileCollection) -> File = FileCollection::getSingleFile

    /**
     * Output directory
     */
    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    /**
     * Executes the task
     */
    @TaskAction
    fun action() {
        val dir = this.outputDirectory.asFile.get()
        if (!dir.isDirectory && !dir.mkdirs())
            throw AssertionError("Directory \"${dir.absolutePath}\" does not exist and could not be created")

        val source: File = this.fileSelector.invoke(this.resolvedSource.outputs.files)
        val targets = this.targets.values

        if (targets.isEmpty()) {
            this.project.logger.log(LogLevel.WARN, "Nothing to do (no targets configured)")
            return
        }

        val files: MutableList<RegularFile> = mutableListOf()
        for (target in targets)
            files += this.buildTarget(dir, source, target)

        this.project.logger.log(LogLevel.INFO, "Successfully built ${files.size} executables")
        for (file in files) {
            this.project.logger.log(LogLevel.DEBUG, "- $file")
        }
    }

    /**
     * Builds a single target
     */
    private fun buildTarget(buildDir: File, source: File, target: PackagerTarget): RegularFile {
        val header = "== Building Target (${target.identifier}) =="
        this.project.logger.log(LogLevel.INFO, header)

        val packager = JARStrap.createPackager(GradleLoggerHandler.wrap(this.project.logger))
        packager.setSource(source)
        packager.setAppName(this.project.name)
        packager.setOutputDir(buildDir)
        target.apply(packager)

        packager.use {
            var stage: String?
            while (true) {
                stage = packager.nextStage()
                if (stage == null) break
                this.buildTargetStage(packager, stage)
                packager.executeStage()
            }
        }

        val outputPath = packager.outputFile.toPath()
        val projectDirectory = this.project.layout.projectDirectory
        val projectOutputPath = projectDirectory.asFile.toPath().relativize(outputPath).toString()

        this.project.logger.log(LogLevel.INFO, "> $projectOutputPath")
        this.project.logger.log(LogLevel.INFO, "=".repeat(header.length))

        return projectDirectory.file(projectOutputPath)
    }

    private fun buildTargetStage(packager: Packager, stage: String) {
        this.project.logger.log(LogLevel.INFO, "- $stage")

        val result = runCatching {
            packager.executeStage()
        }
        if (result.isSuccess) return

        this.project.logger.log(LogLevel.ERROR, "Failed while executing stage: $stage")
        if (!this.project.logger.isInfoEnabled)
            this.project.logger.log(LogLevel.ERROR, "Run with --info or --debug for more info")

        throw result.exceptionOrNull()!!
    }

    /**
     * Creates a new build target with the specified ID
     */
    fun target(id: String, configure: Action<PackagerTarget>) {
        if (this.targets.containsKey(id)) {
            throw IllegalStateException("Target with ID \"$id\" already exists")
        }
        val baseOutputName: String = this.outputName ?: let {
            val appName: String = this.appName ?: this.project.name
            appName.lowercase(Locale.ROOT)
                .replace(Regex("\\s+"), "_")
        }
        val target = PackagerTarget(this, id)
        target.outputName = "$baseOutputName-$id"
        configure.execute(target)
        this.targets[id] = target
    }

    /**
     * Creates a new build target with the specified ID, inheriting the root configuration
     */
    fun target(id: String) {
        this.target(id) { }
    }

}