package io.github.wasabithumb.jarstrap.plugin.extensions

import io.github.wasabithumb.jarstrap.manifest.ManifestMutator

operator fun ManifestMutator.plusAssign(pair: Pair<String, String>) {
    this.put(pair.first, pair.second)
}

operator fun ManifestMutator.minusAssign(key: String) {
    this.remove(key)
}

operator fun ManifestMutator.set(key: String, value: String) {
    this.put(key, value);
}
