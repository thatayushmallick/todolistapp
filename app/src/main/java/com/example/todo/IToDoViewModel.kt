package com.example.todo

import kotlinx.coroutines.flow.StateFlow

interface IToDoViewModel {
    val todoItems: StateFlow<List<ToDoItem>>
    fun addItem(text: String)
    fun toggleDone(item: ToDoItem)
    fun deleteDoneItems()
}
