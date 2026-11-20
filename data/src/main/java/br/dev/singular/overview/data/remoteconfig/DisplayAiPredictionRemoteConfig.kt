package br.dev.singular.overview.data.remoteconfig

import br.dev.singular.overview.data.remoteconfig.RemoteConfigKey.DISPLAY_AI_PREDICTION_KEY

data class DisplayAiPredictionRemoteConfig(
    private val _localOverride: Boolean?,
    private val _remoteSource: IRemoteConfigProvider
) : RemoteConfig<Boolean> {
    override fun execute(): Boolean {
        return _localOverride ?: _remoteSource.getBoolean(DISPLAY_AI_PREDICTION_KEY)
    }
}
