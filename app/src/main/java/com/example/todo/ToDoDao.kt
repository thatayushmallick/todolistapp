package com.example.todo

import androidx.room.*
import kotlinx.coroutines.flow.Flow // For reactive updates

@Dao
interface ToDoDao {
    @Query("SELECT * FROM todo_items ORDER BY id ASC")
    fun getAllItems(): Flow<List<ToDoItem>> // Observe changes as a Flow

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Replace if item with same ID exists
    suspend fun insertItem(item: ToDoItem)

    @Update
    suspend fun updateItem(item: ToDoItem)

    @Query("DELETE FROM todo_items WHERE isDone = 1") // SQLite uses 1 for true, 0 for false
    suspend fun deleteDoneItems()

    // Optional: If you need to delete a specific item by ID
    @Delete
    suspend fun deleteItem(item: ToDoItem)

    @Query("DELETE FROM todo_items WHERE id = :itemId")
    suspend fun deleteItemById(itemId: Int)
}
