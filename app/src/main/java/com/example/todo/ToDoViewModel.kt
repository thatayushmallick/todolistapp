package com.example.todo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// ToDoViewModel now implements IToDoViewModel
class ToDoViewModel(application: Application) : AndroidViewModel(application), IToDoViewModel {

    private val toDoDao = AppDatabase.getDatabase(application).toDoDao()

    // Override is required when implementing an interface property
    override val todoItems: StateFlow<List<ToDoItem>> = toDoDao.getAllItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    override fun addItem(text: String) {
        if (text.isNotBlank()) {
            viewModelScope.launch {
                toDoDao.insertItem(ToDoItem(text = text))
            }
        }
    }

    override fun toggleDone(item: ToDoItem) {
        viewModelScope.launch {
            val updatedItem = item.copy(isDone = !item.isDone)
            toDoDao.updateItem(updatedItem)
        }
    }

    override fun deleteDoneItems() {
        viewModelScope.launch {
            toDoDao.deleteDoneItems()
        }
    }
}
