package br.dev.singular.overview.data.db

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import timber.log.Timber

internal val SEEDS_CALLBACK = object : RoomDatabase.Callback() {
    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        Thread {
            try {
                db.seedMediaTypes()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }.start()
    }
}

private fun SupportSQLiteDatabase.seedMediaTypes() {
    val cursor = query("SELECT COUNT(*) FROM media_type")
    val isEmpty = if (cursor.moveToFirst()) cursor.getInt(0) == 0 else false
    cursor.close()
    if (isEmpty) {
        beginTransaction()
        try {
            execSQL("INSERT OR IGNORE INTO `media_type` (type) VALUES ('movie')")
            execSQL("INSERT OR IGNORE INTO `media_type` (type) VALUES ('tv')")
            setTransactionSuccessful()
        } finally {
            endTransaction()
        }
    }
}
