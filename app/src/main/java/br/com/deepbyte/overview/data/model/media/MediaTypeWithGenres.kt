package br.com.deepbyte.overview.data.model.media

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MediaTypeWithGenres(
    @Embedded val mediaType: MediaTypeEntity,
    @Relation(
        parentColumn = "media_type_db_id",
        entityColumn = "genre_db_id",
        associateBy = Junction(MediaTypeGenresCrossRef::class)
    )
    val genres: List<Genre>
)
