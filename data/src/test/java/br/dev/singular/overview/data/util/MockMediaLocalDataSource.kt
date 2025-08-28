package br.dev.singular.overview.data.util

import br.dev.singular.overview.data.local.source.IMediaLocalDataSource
import br.dev.singular.overview.data.model.MediaDataModel

class MockMediaLocalDataSource : IMediaLocalDataSource {

    override suspend fun insert(models: List<MediaDataModel>) {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): List<MediaDataModel> {
        TODO("Not yet implemented")
    }
}