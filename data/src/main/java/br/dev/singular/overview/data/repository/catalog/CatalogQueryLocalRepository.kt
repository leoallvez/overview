package br.dev.singular.overview.data.repository.catalog

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import br.dev.singular.overview.data.local.source.DataStoreDataSource
import br.dev.singular.overview.data.model.QueryDataState
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.data.util.mappers.domainToData.toData
import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.domain.repository.Get
import br.dev.singular.overview.domain.repository.Observe
import br.dev.singular.overview.domain.repository.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

class CatalogQueryLocalRepository @Inject constructor(
    private val json: Json,
    private val dataSource: DataStoreDataSource,
) : Get<QueryState?>, Update<QueryState?>, Observe<QueryState?> {

    val key: Preferences.Key<String> by lazy {
        stringPreferencesKey(name = "catalog_filters")
    }

    override fun observe(): Flow<QueryState?> = dataSource.getValue(key).map { jsonString ->
        if (jsonString.isNullOrEmpty()) null
        else runCatching {
            json.decodeFromString<QueryDataState>(jsonString).toDomain()
        }.getOrNull()
    }

    override suspend fun get(): QueryState? {
        val jsonString = dataSource.getValue(key).first() ?: return null

        return runCatching {
            json.decodeFromString<QueryDataState>(jsonString).toDomain()
        }.getOrNull()
    }

    override suspend fun update(item: QueryState?) {
        val jsonString = item?.let { json.encodeToString(it.toData()) } ?: ""
        dataSource.setValue(key, jsonString)
    }

}
