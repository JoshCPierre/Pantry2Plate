package com.example.pantry2plate

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecipeAdapter(private val recipes: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val ivRecipeImage: ImageView = view.findViewById(R.id.ivRecipeImage)
        val btnViewIngredients: Button = view.findViewById(R.id.btnViewIngredients)
        val tvIngredients: TextView = view.findViewById(R.id.tvIngredients)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.tvTitle.text = recipe.title

        // Load the image from Spoonacular (now smaller in XML)
        Glide.with(holder.itemView.context)
            .load(recipe.image)
            .into(holder.ivRecipeImage)

        // Reset ingredients visibility
        holder.tvIngredients.visibility = View.GONE
        holder.btnViewIngredients.text = "View Ingredients"

        holder.btnViewIngredients.setOnClickListener {
            if (holder.tvIngredients.visibility == View.GONE) {
                holder.tvIngredients.visibility = View.VISIBLE
                holder.btnViewIngredients.text = "Hide Ingredients"
                
                // Build the highlighted ingredient list
                val builder = SpannableStringBuilder()
                val greenColor = ContextCompat.getColor(holder.itemView.context, R.color.pantry_green)
                val redColor = ContextCompat.getColor(holder.itemView.context, R.color.missing_red)

                // 1. Used Ingredients (Highlighted Green)
                recipe.usedIngredients.forEach { ingredient ->
                    val start = builder.length
                    builder.append("• ${ingredient.original}\n")
                    builder.setSpan(
                        ForegroundColorSpan(greenColor),
                        start,
                        builder.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

                // 2. Missed Ingredients (Highlighted Red)
                recipe.missedIngredients.forEach { ingredient ->
                    val start = builder.length
                    builder.append("• ${ingredient.original}\n")
                    builder.setSpan(
                        ForegroundColorSpan(redColor),
                        start,
                        builder.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

                holder.tvIngredients.text = builder
            } else {
                holder.tvIngredients.visibility = View.GONE
                holder.btnViewIngredients.text = "View Ingredients"
            }
        }
    }

    override fun getItemCount() = recipes.size
}