package io.github.wasabithumb.jarstrap.plugin.config

import io.github.wasabithumb.jarstrap.packager.Packager
import io.github.wasabithumb.jarstrap.packager.PackagerArch
import org.gradle.api.Action
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional

interface DeferringPackagerConfiguration : PackagerConfiguration {

    /**
     * Backing configuration object
     */
    val config: PackagerConfiguration

    //

    @get:Input
    @get:Optional
    override var appName: String?
        get() = this.config.appName
        set(value) {
            this.config.appName = value
        }

    @get:Input
    @get:Optional
    override var arch: PackagerArch?
        get() = this.config.arch
        set(value) {
            this.config.arch = value
        }

    @get:Internal
    override var x64: Boolean
        get() = this.config.x64
        set(value) {
            this.config.x64 = value
        }

    @get:Input
    @get:Optional
    override var autoInstall: Boolean?
        get() = this.config.autoInstall
        set(value) {
            this.config.autoInstall = value
        }

    @get:Input
    @get:Optional
    override var installPrompt: String?
        get() = this.config.installPrompt
        set(value) {
            this.config.installPrompt = value
        }

    @get:Input
    @get:Optional
    override var launchFlags: String?
        get() = this.config.launchFlags
        set(value) {
            this.config.launchFlags = value
        }

    @get:Input
    @get:Optional
    override var outputName: String?
        get() = this.config.outputName
        set(value) {
            this.config.outputName = value
        }

    @get:Input
    @get:Optional
    override var preferredJavaVersion: Int?
        get() = this.config.preferredJavaVersion
        set(value) {
            this.config.preferredJavaVersion = value
        }

    @get:Input
    @get:Optional
    override var release: Boolean?
        get() = this.config.release
        set(value) {
            this.config.release = value
        }

    override fun apply(target: Packager) {
        this.config.apply(target)
    }

    override fun extra(action: Action<Packager>) {
        this.config.extra(action)
    }

    override fun tee(): PackagerConfiguration {
        return this.config.tee()
    }

}