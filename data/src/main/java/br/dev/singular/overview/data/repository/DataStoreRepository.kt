package br.dev.singular.overview.data.repository

import androidx.datastore.preferences.core.Preferences
import br.dev.singular.overview.data.local.source.DataStoreDataSource
import br.dev.singular.overview.domain.repository.Get
import br.dev.singular.overview.domain.repository.Update
import kotlinx.coroutines.flow.first

class DataStoreRepository<T>(
    private val key: Preferences.Key<T>,
    private val dataSource: DataStoreDataSource,
    private val defaultValue: T
) : Get<T>, Update<T> {

    override suspend fun get(): T {
        return dataSource.getValue(key).first() ?: defaultValue
    }

    override suspend fun update(item: T) {
        dataSource.setValue(key, item)
    }
}
