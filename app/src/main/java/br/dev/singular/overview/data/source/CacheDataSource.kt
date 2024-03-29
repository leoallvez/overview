package br.dev.singular.overview.data.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CacheDataSource @Inject constructor(
    private val _dataStore: DataStore<Preferences>
) {

    suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        _dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    fun <T> getValue(key: Preferences.Key<T>): Flow<T?> {
        return _dataStore.data.map { preferences ->
            preferences[key]
        }
    }

    companion object {
        val KEY_FILTER_CACHE = stringPreferencesKey(name = "filter_cache")
        val KEY_SELECTED_STREAMING_CACHE = stringPreferencesKey(name = "selected_streaming_cache")
    }
}
