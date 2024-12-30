package io.github.wasabithumb.jarstrap.plugin.target

import io.github.wasabithumb.jarstrap.plugin.config.DeferringPackagerConfiguration
import io.github.wasabithumb.jarstrap.plugin.config.PackagerConfiguration

class PackagerTarget(
    rootConfig: PackagerConfiguration,
    val identifier: String
) : DeferringPackagerConfiguration {

    override val config: PackagerConfiguration = rootConfig.tee()

}
