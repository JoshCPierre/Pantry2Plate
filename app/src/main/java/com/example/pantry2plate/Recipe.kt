package com.example.pantry2plate

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val usedIngredientCount: Int,
    val missedIngredientCount: Int
)