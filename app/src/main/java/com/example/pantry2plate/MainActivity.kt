package com.example.pantry2plate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.pantry2plate.ui.theme.Pantry2PlateTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // --- TEST THE API ---
        lifecycleScope.launch {
            try {
                val recipes = RetrofitInstance.api.findByIngredients(
                    ingredients = "chicken,rice",
                    apiKey = BuildConfig.SPOONACULAR_KEY
                )

                if (recipes.isNotEmpty()) {
                    Log.d("API_SUCCESS", "Top Recipe: ${recipes[0].title}")
                    Log.d("API_SUCCESS", "Image URL: ${recipes[0].image}")
                } else {
                    Log.d("API_SUCCESS", "Call worked, but 0 recipes found.")
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Network call failed!", e)
            }
        }
        // --------------------

        setContent {
            Pantry2PlateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

        binding.loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.registerButton.setOnClickListener {
            // later: go to RegisterActivity
        }
    }
}