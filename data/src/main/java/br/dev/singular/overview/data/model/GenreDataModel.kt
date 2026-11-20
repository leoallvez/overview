package br.dev.singular.overview.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "genre")
data class GenreDataModel(
    @PrimaryKey
    val id: Long = 0,
    val name: String = ""
)
