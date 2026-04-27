package com.example.pantry2plate

import android.content.res.ColorStateList
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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RecipeAdapter(
    private var recipes: List<Recipe>,
    private val isSavedList: Boolean = false
) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var savedRecipeIds = mutableSetOf<Int>()

    init {
        fetchSavedRecipes()
    }

    private fun fetchSavedRecipes() {
        db.collection("saved_recipes").addSnapshotListener { snapshots, _ ->
            if (snapshots != null) {
                savedRecipeIds.clear()
                for (doc in snapshots) {
                    val id = doc.getLong("id")?.toInt()
                    if (id != null) savedRecipeIds.add(id)
                }
                notifyDataSetChanged()
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val ivRecipeImage: ImageView = view.findViewById(R.id.ivRecipeImage)
        val btnViewIngredients: Button = view.findViewById(R.id.btnViewIngredients)
        val tvIngredients: TextView = view.findViewById(R.id.tvIngredients)
        val btnAction: Button = view.findViewById(R.id.btnAction)
        val tvSavedBy: TextView = view.findViewById(R.id.tvSavedBy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.tvTitle.text = recipe.title

        Glide.with(holder.itemView.context)
            .load(recipe.image)
            .into(holder.ivRecipeImage)

        holder.tvIngredients.visibility = View.GONE
        holder.btnViewIngredients.text = "View Ingredients"

        val isSaved = savedRecipeIds.contains(recipe.id)
        val greenColor = ContextCompat.getColor(holder.itemView.context, R.color.pantry_green)
        val redColor = ContextCompat.getColor(holder.itemView.context, R.color.missing_red)

        if (isSavedList) {
            holder.tvSavedBy.visibility = View.VISIBLE
            holder.tvSavedBy.text = "Saved by: ${recipe.savedBy ?: "Unknown"}"
            holder.btnAction.text = "Remove"
            holder.btnAction.backgroundTintList = ColorStateList.valueOf(redColor)
        } else {
            holder.tvSavedBy.visibility = View.GONE
            if (isSaved) {
                holder.btnAction.text = "Remove"
                holder.btnAction.backgroundTintList = ColorStateList.valueOf(redColor)
            } else {
                holder.btnAction.text = "Add"
                holder.btnAction.backgroundTintList = ColorStateList.valueOf(greenColor)
            }
        }

        holder.btnAction.setOnClickListener {
            val user = auth.currentUser
            if (user == null) {
                Toast.makeText(holder.itemView.context, "Please log in first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isSavedList || (isSaved && !isSavedList)) {
                // Remove logic
                db.collection("saved_recipes").document(recipe.id.toString()).delete()
                    .addOnSuccessListener {
                        Toast.makeText(holder.itemView.context, "Recipe removed", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // Save logic
                val recipeData = hashMapOf(
                    "id" to recipe.id,
                    "title" to recipe.title,
                    "image" to recipe.image,
                    "savedBy" to (user.email ?: "User"),
                    "usedIngredients" to recipe.usedIngredients,
                    "missedIngredients" to recipe.missedIngredients
                )

                db.collection("saved_recipes").document(recipe.id.toString()).set(recipeData)
                    .addOnSuccessListener {
                        Toast.makeText(holder.itemView.context, "Recipe added!", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        holder.btnViewIngredients.setOnClickListener {
            if (holder.tvIngredients.visibility == View.GONE) {
                holder.tvIngredients.visibility = View.VISIBLE
                holder.btnViewIngredients.text = "Hide Ingredients"
                
                val builder = SpannableStringBuilder()

                recipe.usedIngredients.forEach { ingredient ->
                    val start = builder.length
                    builder.append("• ${ingredient.original}\n")
                    builder.setSpan(ForegroundColorSpan(greenColor), start, builder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }

                recipe.missedIngredients.forEach { ingredient ->
                    val start = builder.length
                    builder.append("• ${ingredient.original}\n")
                    builder.setSpan(ForegroundColorSpan(redColor), start, builder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }

                holder.tvIngredients.text = builder
            } else {
                holder.tvIngredients.visibility = View.GONE
                holder.btnViewIngredients.text = "View Ingredients"
            }
        }
    }

    fun updateRecipes(newRecipes: List<Recipe>) {
        this.recipes = newRecipes
        notifyDataSetChanged()
    }

    override fun getItemCount() = recipes.size
}