package com.example.todo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_items") // Define the table name
data class ToDoItem(
    @PrimaryKey(autoGenerate = true) // Make id the primary key and auto-generate it
    val id: Int = 0, // Default value needed for autoGenerate
    val text: String,
    var isDone: Boolean = false
)
