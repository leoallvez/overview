package br.com.deepbyte.overview.data.repository

import br.com.deepbyte.overview.BuildConfig
import br.com.deepbyte.overview.data.source.CacheDataSource
import br.com.deepbyte.overview.data.source.CacheDataSource.Companion.LAST_CACHE_TIME
import br.com.deepbyte.overview.data.source.suggestion.SuggestionLocalDataSource
import br.com.deepbyte.overview.data.source.suggestion.SuggestionRemoteDataSource
import br.com.deepbyte.overview.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.abs

class SuggestionRepository @Inject constructor(
    private var _cacheDataSource: CacheDataSource,
    private val _localDataSource: SuggestionLocalDataSource,
    private val _remoteDataSource: SuggestionRemoteDataSource,
    @IoDispatcher private val _ioDispatcher: CoroutineDispatcher
) {
    private val _formatter: DateFormat by lazy {
        SimpleDateFormat(TIME_PATTERN, Locale.ENGLISH)
    }

    suspend fun refresh() = withContext(_ioDispatcher) {
        if (isTimeToRefreshCache()) {
            val suggestions = _remoteDataSource.getItems().toTypedArray()
            _localDataSource.update(*suggestions)
            saveLastCacheTime()
        }
    }

    private suspend fun saveLastCacheTime() {
        val nowDateFormatted = _formatter.format(Date())
        _cacheDataSource.setValue(LAST_CACHE_TIME, nowDateFormatted)
    }

    private suspend fun isTimeToRefreshCache(): Boolean {
        val lastCacheDateFormatted = getLastCacheDateFormatted()
        return if (lastCacheDateFormatted.isNotBlank()) {
            val lastCacheDate = _formatter.parse(lastCacheDateFormatted) as Date
            return maximumCacheTimeHasPassed(lastCacheDate)
        } else {
            true
        }
    }

    private suspend fun getLastCacheDateFormatted(): String {
        return _cacheDataSource.getValue(LAST_CACHE_TIME).first() ?: ""
    }

    private fun maximumCacheTimeHasPassed(
        lastCacheDate: Date
    ): Boolean {
        val diffInMilliseconds = abs(lastCacheDate.time - Date().time)
        val diffInHours = TimeUnit.HOURS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS)
        return diffInHours > MAXIMUM_CACHE_HOURS || BuildConfig.DEBUG
    }

    companion object {
        private const val TIME_PATTERN = "MM/dd/yyyy HH:mm:ss"
        private const val MAXIMUM_CACHE_HOURS = 24L
    }
}
