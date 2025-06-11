package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.ActivityMainBinding
import kotlin.text.isNotEmpty

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // Declare the binding variable
    private lateinit var todoAdapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // Inflate the layout
        setContentView(binding.root) // Set the content view to the root of the binding

        todoAdapter = Adapter(mutableListOf())

        binding.itemList.apply { // Access RecyclerView via binding
            adapter = todoAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        binding.addButton.setOnClickListener { // Access Button via binding
            val todoTitle = binding.newItem.text.toString() // Access EditText via binding
            if (todoTitle.isNotEmpty()) {
                val todo = Item(todoTitle)
                todoAdapter.addTodo(todo)
                binding.newItem.text.clear() // Clear the EditText
            }
        }

        binding.deleteButton.setOnClickListener { // Access Button via binding
            todoAdapter.deleteDoneTodos()
        }
    }
}