package io.github.leoallvez.take.data.repository

import io.github.leoallvez.take.data.source.CacheDataSource
import io.github.leoallvez.take.data.source.CacheDataSource.Companion.LAST_CACHE_TIME
import io.github.leoallvez.take.data.source.suggestion.SuggestionLocalDataSource
import io.github.leoallvez.take.data.source.suggestion.SuggestionRemoteDataSource
import io.github.leoallvez.take.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.abs

class SuggestionRepository @Inject constructor(
    private var cacheDataSource: CacheDataSource,
    private val localDataSource: SuggestionLocalDataSource,
    private val remoteDataSource: SuggestionRemoteDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    private val formatter: DateFormat by lazy {
        SimpleDateFormat(TIME_PATTERN)
    }

    suspend fun refresh() = withContext (ioDispatcher) {
        if(isTimeToRefreshCache()) {
            val suggestions = remoteDataSource.get().toTypedArray()
            localDataSource.deleteAll()
            localDataSource.save(*suggestions)
            saveLastCacheTime()
        }
    }

    private suspend fun saveLastCacheTime() {
        val nowDateString = formatter.format(Date())
        cacheDataSource.setValue(LAST_CACHE_TIME, nowDateString)
    }

    private suspend fun isTimeToRefreshCache(): Boolean {
        val lastCacheDateFormatted = cacheDataSource.getValue(LAST_CACHE_TIME).first()
        val hasLastCacheFormatted = lastCacheDateFormatted.isNullOrBlank().not()
        return if(hasLastCacheFormatted) {
            val lastCacheDate = formatter.parse(lastCacheDateFormatted)
            return hasMoreThan24Hours(lastCacheDate)
        } else {
            true
        }
    }

    private fun hasMoreThan24Hours(lastCacheDate: Date): Boolean {
        val diffInMilliseconds = abs(lastCacheDate.time - Date().time)
        val diffInHours = TimeUnit.HOURS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS)
        return diffInHours > MAXIMUM_CACHE_HOURS
    }

    companion object {
        private const val TIME_PATTERN = "MM/dd/yyyy HH:mm:ss"
        private const val MAXIMUM_CACHE_HOURS = 24
    }
}
