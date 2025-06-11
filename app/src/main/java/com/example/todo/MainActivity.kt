package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {
    private val toDoViewModel: ToDoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Ensure AppTheme is correctly defined in com.example.todo.ui.theme.AppTheme
            AppTheme {
                ToDoScreen(toDoViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoScreen(viewModel: IToDoViewModel = viewModel<ToDoViewModel>()) { // Expect Interface, default to real VM
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    val items by viewModel.todoItems.collectAsState()

    // ... rest of your ToDoScreen code remains the same
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My ToDo Tasks") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (textState.text.isNotBlank()) {
                    viewModel.addItem(textState.text)
                    textState = TextFieldValue("")
                }
            }) {
                Icon(Icons.Filled.Add, "Add ToDo")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textState,
                    onValueChange = { textState = it },
                    label = { Text("New Task") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (items.any { it.isDone }) {
                Button(
                    onClick = { viewModel.deleteDoneItems() },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete Selected Items")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete Selected Items")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (items.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No tasks yet. Add some!")
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(items, key = { it.id }) { item ->
                        ToDoRow(item = item, onItemClicked = { viewModel.toggleDone(item) })
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
fun ToDoRow(item: ToDoItem, onItemClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClicked)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isDone,
            onCheckedChange = { onItemClicked() }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = item.text,
            style = if (item.isDone) LocalTextStyle.current.copy(textDecoration = TextDecoration.LineThrough)
            else LocalTextStyle.current
        )
    }
}

@Preview(showBackground = true, name = "ToDo Screen Preview")
@Composable
fun DefaultPreview() {
    // This FakeViewModel implements the IToDoViewModel interface
    class FakePreviewToDoViewModel : IToDoViewModel {
        private val _items = MutableStateFlow(
            listOf(
                ToDoItem(id = 1, text = "Preview Buy groceries", isDone = true),
                ToDoItem(id = 2, text = "Preview Walk the dog", isDone = false),
                ToDoItem(id = 3, text = "Preview Read a book", isDone = false)
            )
        )
        override val todoItems: StateFlow<List<ToDoItem>> = _items

        override fun addItem(text: String) {
            if (text.isNotBlank()) {
                val newItem = ToDoItem(id = (_items.value.maxOfOrNull { it.id } ?: 0) + 1, text = text)
                _items.value = _items.value + newItem
            }
        }

        override fun toggleDone(item: ToDoItem) {
            _items.value = _items.value.map {
                if (it.id == item.id) it.copy(isDone = !it.isDone) else it
            }
        }

        override fun deleteDoneItems() {
            _items.value = _items.value.filter { !it.isDone }
        }
    }

    AppTheme {
        // Pass the instance of the fake ViewModel that implements the interface
        ToDoScreen(viewModel = FakePreviewToDoViewModel())
    }
}
