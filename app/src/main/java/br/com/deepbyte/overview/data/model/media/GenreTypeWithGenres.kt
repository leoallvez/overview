package br.com.deepbyte.overview.data.model.media

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class GenreTypeWithGenres(
    @Embedded val genreType: GenreType,
    @Relation(
        parentColumn = "genre_type_db_id",
        entityColumn = "genre_db_id",
        associateBy = Junction(GenreTypeCrossRef::class)
    )
    val genres: List<Genre>
)
