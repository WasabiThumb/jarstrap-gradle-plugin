package io.github.wasabithumb.jarstrap.plugin.config

import io.github.wasabithumb.jarstrap.manifest.ManifestMutator
import io.github.wasabithumb.jarstrap.packager.Packager
import io.github.wasabithumb.jarstrap.packager.PackagerArch
import org.gradle.api.Action

interface PackagerConfiguration {

    /**
     * If not null, updates the packager's app name.
     * @see Packager.appName
     */
    var appName: String?

    /**
     * If not null, updates the packager's target architecture.
     * @see Packager.getArch
     */
    var arch: PackagerArch?

    /**
     * Wrapper around ``arch``, acting as a switch between X86 and X86_64
     * @see arch
     */
    var x64: Boolean
        get() = PackagerArch.X86 != this.arch
        set(value) {
            this.arch = if (value) PackagerArch.X86_64 else PackagerArch.X86
        }

    /**
     * If not null, updates the packager's auto install option.
     * @see Packager.isAutoInstall
     */
    var autoInstall: Boolean?

    /**
     * If not null, updates the packager's release option.
     * @see Packager.isRelease
     */
    var release: Boolean?

    /**
     * If not null, updates the output name.
     * @see Packager.getOutputName
     */
    var outputName: String?

    /**
     * If not null, updates the launch flags.
     * @see Packager.launchFlags
     */
    var launchFlags: String?

    /**
     * If not null, updates the preferred java version.
     * @see Packager.getPreferredJavaVersion
     */
    var preferredJavaVersion: Int?

    /**
     * If not null, updates the installation prompt.
     * @see Packager.getInstallPrompt
     */
    var installPrompt: String?

    //

    /**
     * Perform custom operations on the underlying ``Packager``.
     * Should not be necessary.
     */
    fun extra(action: Action<Packager>)

    /**
     * Mutates the manifest of the source JAR before bootstrap.
     * Setting the ``Main-Class`` here will make the JAR runnable.
     * @see ManifestMutator.put
     */
    fun manifest(action: Action<ManifestMutator>) {
        this.extra {
            action.execute(this.manifest)
        }
    }

    //

    /**
     * Applies any set properties to the specified packager.
     */
    fun apply(target: Packager)

    /**
     * Creates a shallow copy of this configuration.
     */
    fun tee(): PackagerConfiguration

    //

    companion object {

        fun create(): PackagerConfiguration {
            return BasicPackagerConfiguration()
        }

    }

}