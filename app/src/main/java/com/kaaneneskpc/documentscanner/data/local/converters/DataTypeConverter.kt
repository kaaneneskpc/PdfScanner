package com.kaaneneskpc.documentscanner.data.local.converters

import androidx.room.TypeConverter
import java.util.Date

class DataTypeConverter {

    @TypeConverter
    fun fromTimeStamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimeStamp(date: Date?): Long? {
        return date?.time
    }

}