package br.dev.singular.overview.data.db

import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import timber.log.Timber

class Migration(
    private val from: Int,
    private val to: Int,
    private val context: Context
) : Migration(from, to) {
    override fun migrate(db: SupportSQLiteDatabase) {
        try {
            val fileName = "database/migrations/migration_${from}_to_${to}.sql"
            db.runScriptFromAssets(context, fileName)
            Timber.d("Migration $from to $to executed successfully")
        } catch (e: Exception) {
            Timber.e(e, "Migration $from to $to failed")
        }
    }
}
