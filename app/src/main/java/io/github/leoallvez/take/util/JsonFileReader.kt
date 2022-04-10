package io.github.leoallvez.take.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class JsonFileReader @Inject constructor(
    @ApplicationContext private val context: Context,
) : IJsonFileReader {

    override fun read(filePath: String): String {
        return try {
            context.assets.open(filePath)
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            Timber.d(ioException)
            ""
        }
    }
}