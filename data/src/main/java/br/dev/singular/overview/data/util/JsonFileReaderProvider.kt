package br.dev.singular.overview.data.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

interface IJsonFileReaderProvider {
    fun read(filePath: String): String
}

class JsonFileReaderProvider @Inject constructor(
    @param:ApplicationContext
    private val context: Context
) : IJsonFileReaderProvider {

    override fun read(filePath: String): String {
        return try {
            context.assets.open(filePath).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            Timber.d(ioException)
            ""
        }
    }
}
