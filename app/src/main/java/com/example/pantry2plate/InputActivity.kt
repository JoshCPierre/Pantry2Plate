package com.example.pantry2plate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class InputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Link this code to your XML file
        setContentView(R.layout.activity_input)

        val etIngredients = findViewById<EditText>(R.id.etIngredients)
        val btnSearch = findViewById<Button>(R.id.btnSearch)

        btnSearch.setOnClickListener {
            val ingredients = etIngredients.text.toString()

            if (ingredients.isNotBlank()) {
                // Navigate to the Results screen and hand off the string
                val intent = Intent(this, ResultsActivity::class.java)
                intent.putExtra("INGREDIENTS_KEY", ingredients)
                startActivity(intent)
            }
        }
    }
}