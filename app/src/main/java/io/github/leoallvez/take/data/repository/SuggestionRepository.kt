package io.github.leoallvez.take.data.repository

import android.util.Log
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
        Log.i("refresh_cache_debug", "salve date: $nowDateString")
        cacheDataSource.setValue(LAST_CACHE_TIME, nowDateString)
    }

    private suspend fun isTimeToRefreshCache(): Boolean {
        val lastCacheDateString: String? = cacheDataSource.getValue(LAST_CACHE_TIME).first()
        return if(lastCacheDateString.isNullOrBlank().not()) {
            val nowDate = Date()
            val lastCacheDate = formatter.parse(lastCacheDateString)
            val diffInMilliseconds: Long = abs(lastCacheDate.time - nowDate.time)
            val diff: Long = TimeUnit.HOURS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS)
            Log.i("refresh_cache_debug", "diff: $diff")
            return diff > 23
        } else {
            true
        }
    }

    companion object {
        private const val TIME_PATTERN = "MM/dd/yyyy HH:mm:ss"
    }

}