package com.example.pantry2plate

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val usedIngredientCount: Int = 0,
    val missedIngredientCount: Int = 0,
    val usedIngredients: List<Ingredient> = emptyList(),
    val missedIngredients: List<Ingredient> = emptyList(),
    val savedBy: String? = null
)

data class Ingredient(
    val id: Int = 0,
    val amount: Double = 0.0,
    val unit: String = "",
    val unitLong: String = "",
    val unitShort: String = "",
    val aisle: String = "",
    val name: String = "",
    val original: String = "",
    val originalName: String = "",
    val meta: List<String> = emptyList(),
    val image: String = ""
)