package com.example.pantry2plate

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class SavedRecipesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipeAdapter
    private val db = FirebaseFirestore.getInstance()
    private var savedRecipesListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_recipes)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        recyclerView = findViewById(R.id.rvSavedRecipes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        adapter = RecipeAdapter(emptyList(), isSavedList = true)
        recyclerView.adapter = adapter

        setupFirebaseListener()
    }

    private fun setupFirebaseListener() {
        savedRecipesListener = db.collection("saved_recipes")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("Firestore", "Listen failed.", e)
                    return@addSnapshotListener
                }

                val savedRecipes = mutableListOf<Recipe>()
                for (doc in snapshots!!) {
                    try {
                        val id = doc.getLong("id")?.toInt() ?: 0
                        val title = doc.getString("title") ?: ""
                        val image = doc.getString("image") ?: ""
                        val savedBy = doc.getString("savedBy") ?: ""
                        
                        // Parse ingredients lists
                        val usedIngredientsRaw = doc.get("usedIngredients") as? List<Map<String, Any>> ?: emptyList()
                        val missedIngredientsRaw = doc.get("missedIngredients") as? List<Map<String, Any>> ?: emptyList()
                        
                        val usedIngredients = usedIngredientsRaw.map { mapIngredient(it) }
                        val missedIngredients = missedIngredientsRaw.map { mapIngredient(it) }

                        savedRecipes.add(Recipe(
                            id = id,
                            title = title,
                            image = image,
                            usedIngredients = usedIngredients,
                            missedIngredients = missedIngredients,
                            savedBy = savedBy
                        ))
                    } catch (ex: Exception) {
                        Log.e("Firestore", "Error parsing recipe", ex)
                    }
                }
                adapter.updateRecipes(savedRecipes)
            }
    }

    private fun mapIngredient(data: Map<String, Any>): Ingredient {
        return Ingredient(
            id = (data["id"] as? Long)?.toInt() ?: 0,
            name = data["name"] as? String ?: "",
            original = data["original"] as? String ?: "",
            amount = (data["amount"] as? Number)?.toDouble() ?: 0.0,
            unit = data["unit"] as? String ?: ""
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        savedRecipesListener?.remove()
    }
}