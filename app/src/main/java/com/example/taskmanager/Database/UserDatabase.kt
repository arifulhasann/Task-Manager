package com.example.taskmanager.Database
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [User::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class UserDatabase() : RoomDatabase (){
    abstract fun getUserDao():UserDao
}