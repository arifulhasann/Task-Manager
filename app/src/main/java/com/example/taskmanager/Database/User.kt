package com.example.taskmanager.Database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.parcelize.Parcelize
import java.util.Date


@Parcelize
@Entity(tableName = "task_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val title:String,
    val note:String,
    val date: Date,
    val status:String
):Parcelable

class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
