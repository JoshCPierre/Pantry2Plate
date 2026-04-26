package com.example.pantry2plate

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val usedIngredientCount: Int,
    val missedIngredientCount: Int,
    val usedIngredients: List<Ingredient>,
    val missedIngredients: List<Ingredient>
)

data class Ingredient(
    val id: Int,
    val amount: Double,
    val unit: String,
    val unitLong: String,
    val unitShort: String,
    val aisle: String,
    val name: String,
    val original: String,
    val originalName: String,
    val meta: List<String>,
    val image: String
)