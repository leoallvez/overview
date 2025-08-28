package br.dev.singular.overview.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `media` (
                `id` INTEGER PRIMARY KEY NOT NULL,
                `name` TEXT NOT NULL,
                `title` TEXT NOT NULL,
                `poster_path` TEXT NOT NULL,
                `type` TEXT NOT NULL,
                `is_liked` INTEGER NOT NULL,
                `last_update` INTEGER NOT NULL
            )
        """.trimIndent())
    }
}