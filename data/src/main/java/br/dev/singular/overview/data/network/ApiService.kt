package br.dev.singular.overview.data.network

import br.dev.singular.overview.data.BuildConfig

interface ApiService {

    private companion object {
        const val API_KEY = BuildConfig.API_KEY
    }
}
