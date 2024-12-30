package io.github.wasabithumb.jarstrap.plugin.config

import io.github.wasabithumb.jarstrap.packager.Packager
import io.github.wasabithumb.jarstrap.packager.PackagerArch
import org.gradle.api.Action

internal data class BasicPackagerConfiguration(
    override var appName: String?,
    override var arch: PackagerArch?,
    override var autoInstall: Boolean?,
    override var release: Boolean?,
    override var outputName: String?,
    override var launchFlags: String?,
    override var preferredJavaVersion: Int?,
    override var installPrompt: String?
): PackagerConfiguration {

    private val custom: MutableList<Action<Packager>> = mutableListOf()

    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )

    //

    override fun extra(action: Action<Packager>) {
        this.custom.add(action)
    }

    override fun apply(target: Packager) {
        this.appName?.let(target::setAppName)
        this.arch?.let(target::setArch)
        this.autoInstall?.let(target::setAutoInstall)
        this.release?.let(target::setRelease)
        this.outputName?.let(target::setOutputName)
        this.launchFlags?.let(target::setLaunchFlags)
        this.preferredJavaVersion?.let(target::setPreferredJavaVersion)
        this.installPrompt?.let(target::setInstallPrompt)
        for (action in this.custom) {
            action.execute(target)
        }
    }

    override fun tee(): PackagerConfiguration {
        val ret = this.copy()
        ret.custom.addAll(this.custom)
        return ret
    }

}
