package com.example.gli007.moviedatabase

import androidx.room.TypeConverter
import java.util.*

class DateConverter {

    @TypeConverter
    fun fromTimestap(value: Long?): Date? {
      return if( value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
       return date.time
    }

}