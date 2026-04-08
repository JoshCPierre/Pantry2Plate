package com.example.pantry2plate

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {
    var recipesList by mutableStateOf<List<Recipe>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun searchRecipes(ingredients: String) {
        // prevent searching if  already loading or ingredients are empty
        if (ingredients.isBlank()) return

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val results = RetrofitInstance.api.findByIngredients(
                    ingredients = ingredients,
                    apiKey = BuildConfig.SPOONACULAR_KEY
                )
                recipesList = results

                if (results.isEmpty()) {
                    errorMessage = "No recipes found for those ingredients."
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "API Error", e)
                errorMessage = "Failed to load recipes. Check your internet."
            } finally {
                isLoading = false
            }
        }
    }
}