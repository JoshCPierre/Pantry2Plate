package com.example.pantry2plate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class PantryAdapter(
    private val onDeleteClick: (docId: String, name: String) -> Unit
) : ListAdapter<PantryItem, PantryAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvIngredientName)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDeleteIngredient)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pantry, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.tvName.text = item.name.replaceFirstChar { it.uppercase() }
        holder.btnDelete.setOnClickListener {
            onDeleteClick(item.docId, item.name)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<PantryItem>() {
        override fun areItemsTheSame(old: PantryItem, new: PantryItem) = old.docId == new.docId
        override fun areContentsTheSame(old: PantryItem, new: PantryItem) = old == new
    }
}