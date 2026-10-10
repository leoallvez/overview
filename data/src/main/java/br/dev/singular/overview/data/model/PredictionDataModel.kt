package br.dev.singular.overview.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "prediction", primaryKeys = ["type", "id"])
data class PredictionDataModel(
    val id: Long = 0,
    val type: MediaDataType = MediaDataType.UNKNOWN,
    @ColumnInfo(name = "affinity_percentage")
    val affinityPercentage: Int? = null,
)
