package br.dev.singular.overview.data.source.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.dev.singular.overview.data.local.source.CacheDataSource
import br.dev.singular.overview.data.local.source.CacheDataSource.Companion.KEY_LAST_CACHE_TIME
import br.dev.singular.overview.domain.usecase.suggestion.IDeleteSuggestionsUseCase
import br.dev.singular.overview.BuildConfig
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs

@HiltWorker
class DeleteSuggestionsCacheWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val cacheDataSource: CacheDataSource,
    private val deleteSuggestionsUseCase: IDeleteSuggestionsUseCase
) : CoroutineWorker(context, params) {

    private val formatter: DateFormat by lazy { SimpleDateFormat(TIME_PATTERN, Locale.ENGLISH) }

    override suspend fun doWork(): Result {
        if (shouldDeleteCache()) {
            deleteSuggestionsUseCase.invoke()
            freshCacheTimestamp()
        }
        return Result.success()
    }

    private suspend fun freshCacheTimestamp() {
        val nowTimestamp = formatter.format(Date())
        cacheDataSource.setValue(KEY_LAST_CACHE_TIME, nowTimestamp)
    }

    private suspend fun shouldDeleteCache(): Boolean {
        val cachingTimestamp = getCachingTimestamp()
        return when {
            cachingTimestamp.isBlank() -> true
            else -> {
                val cachingDate = formatter.parse(cachingTimestamp)
                hasExceededMaxCacheDuration(cachingDate as Date)
            }
        }
    }

    private suspend fun getCachingTimestamp() =
        cacheDataSource.getValue(KEY_LAST_CACHE_TIME).first().orEmpty()

    private fun hasExceededMaxCacheDuration(lastCacheDate: Date): Boolean {
        val diffInMilliseconds = abs(lastCacheDate.time - Date().time)
        val diffInHours = TimeUnit.HOURS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS)
        return diffInHours > MAXIMUM_CACHE_HOURS || BuildConfig.DEBUG
    }

    private companion object {
        const val TIME_PATTERN = "MM/dd/yyyy HH:mm:ss"
        const val MAXIMUM_CACHE_HOURS = 24L
    }
}
