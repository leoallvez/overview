package br.dev.singular.overview.data.db

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import timber.log.Timber

internal fun SupportSQLiteDatabase.runScriptFromAssets(context: Context, fileName: String) {
    try {
        val inputStream = context.assets.open(fileName)
        val script = inputStream.bufferedReader().use { it.readText() }
        val commands = script.split(";")

        beginTransaction()
        try {
            for (command in commands) {
                val trimmedCommand = command.trim()
                if (trimmedCommand.isNotEmpty()) {
                    execSQL(trimmedCommand)
                }
            }
            setTransactionSuccessful()
        } finally {
            endTransaction()
        }
    } catch (e: Exception) {
        Timber.e(e, "Error executing SQL script from file: $fileName")
    }
}
