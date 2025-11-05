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
            );
        """.trimIndent())

        db.execSQL("""
           INSERT INTO media (
               id,
               name,
               title,
               poster_path,
               type,
               is_liked,
               last_update
           )
           SELECT
               m.api_id,
               m.letter, 
               m.letter, 
               m.poster_path,
               m.type,
               m.is_liked,
               m.last_update
           FROM medias m
           WHERE NOT EXISTS (
               SELECT
                  1
               FROM
                  media
               WHERE
                  media.id = m.api_id
           );
        """.trimIndent())

        db.execSQL("DROP TABLE medias;")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE suggestion ADD COLUMN `last_update` INTEGER NOT NULL DEFAULT 0;")
    }
}