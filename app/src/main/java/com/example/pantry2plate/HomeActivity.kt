package com.example.pantry2plate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btnGoToPantry = findViewById<Button>(R.id.btnGoToPantry)
        val btnViewSavedRecipes = findViewById<Button>(R.id.btnViewSavedRecipes)

        btnGoToPantry.setOnClickListener {
            val intent = Intent(this, SharedPantryActivity::class.java)
            startActivity(intent)
        }

        btnViewSavedRecipes.setOnClickListener {
            val intent = Intent(this, SavedRecipesActivity::class.java)
            startActivity(intent)
        }
    }
}