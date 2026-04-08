package com.example.pantry2plate

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class ResultsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // 1. Catch the ingredients you passed from the InputActivity
        val ingredients = intent.getStringExtra("INGREDIENTS_KEY") ?: ""

        // 2. Call the Spoonacular API
        lifecycleScope.launch {
            progressBar.visibility = View.VISIBLE
            try {
                val recipes = RetrofitInstance.api.findByIngredients(
                    ingredients = ingredients,
                    apiKey = BuildConfig.SPOONACULAR_KEY
                )
                progressBar.visibility = View.GONE

                // 3. Give the data to the adapter so it shows up on screen
                recyclerView.adapter = RecipeAdapter(recipes)
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                // (Error handling goes here later)
            }
        }
    }
}