package br.dev.singular.overview.data.local.database

import androidx.room.TypeConverter
import br.dev.singular.overview.data.model.MediaDataType
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromMediaDataType(type: MediaDataType): String {
        return type.value
    }

    @TypeConverter
    fun toMediaDataType(value: String): MediaDataType {
        return MediaDataType.fromValue(value)
    }
}
