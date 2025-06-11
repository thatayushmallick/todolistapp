package com.example.todolist

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ItemLayoutBinding

class Adapter(
    private val itemList: MutableList<Item>
) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    // Make sure your Item class is defined somewhere, e.g.:
    // data class Item(val title: String, var isDone: Boolean)

    inner class ViewHolder(val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    fun addTodo(item: Item) {
        itemList.add(item)
        notifyItemInserted(itemList.size - 1)
    }

    fun deleteDoneTodos() {
        val oldSize = itemList.size
        itemList.removeAll { item ->
            item.isDone
        }
        // More efficient to notify about the range of removed items if possible,
        // but notifyDataSetChanged() is simpler for now.
        // For better performance, consider calculating specific removal indices.
        if (itemList.size < oldSize) {
            notifyDataSetChanged() // Or more specific notifications
        }
    }

    private fun toggleStrikeThrough(textView: TextView, isDone: Boolean) {
        if (isDone) {
            textView.paintFlags = textView.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            textView.paintFlags = textView.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curItem = itemList[position]
        holder.binding.apply {
            // Assuming your ItemLayoutBinding has views with these IDs:
            // itemV (TextView) and checkB (CheckBox)
            itemV.text = curItem.title
            checkB.isChecked = curItem.isDone
            toggleStrikeThrough(itemV, curItem.isDone)

            // Set listener to null first to prevent unwanted calls during binding
            checkB.setOnCheckedChangeListener(null)
            checkB.isChecked = curItem.isDone // Re-set after clearing listener

            checkB.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(itemV, isChecked)
                // Ensure you update the underlying data item
                if (holder.adapterPosition != RecyclerView.NO_POSITION) { // Good practice
                    itemList[holder.adapterPosition].isDone = isChecked
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}