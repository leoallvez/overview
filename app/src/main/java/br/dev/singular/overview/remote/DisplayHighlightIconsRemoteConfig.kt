package br.dev.singular.overview.remote

import br.dev.singular.overview.core.remote.RemoteConfigKey.*
import br.dev.singular.overview.core.remote.RemoteConfigProvider

class DisplayHighlightIconsRemoteConfig(
    private val _remoteSource: RemoteConfigProvider
) : RemoteConfig<Boolean> {
    override fun execute() = _remoteSource.getBoolean(DISPLAY_HIGHLIGHT_ICONS_KEY)
}
