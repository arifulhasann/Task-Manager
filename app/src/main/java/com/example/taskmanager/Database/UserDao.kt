package com.example.taskmanager.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    fun addUser(user: User)

    @Update
    fun editUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT * FROM task_table ORDER BY date ASC")
    fun getAllUsers():List<User>

    @Query("SELECT * FROM task_table WHERE title LIKE :searchQuery OR note LIKE :searchQuery")
    suspend fun searchDatabase(searchQuery: String): List<User>
}