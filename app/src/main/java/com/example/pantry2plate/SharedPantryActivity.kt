package com.example.pantry2plate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantry2plate.databinding.ActivitySharedPantryBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore

class SharedPantryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySharedPantryBinding
    private val db = Firebase.firestore
    private var pantryListener: ListenerRegistration? = null
    private lateinit var pantryAdapter: PantryAdapter

    private val ingredientDocMap = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharedPantryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle Back Button
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupDropdown()
        setupRecyclerView()
        setupFirebaseListener()

        binding.btnAdd.setOnClickListener {
            val ingredient = binding.autoCompleteIngredient.text.toString().trim().lowercase()
            if (ingredient.isBlank()) {
                Toast.makeText(this, "Please enter an ingredient", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (ingredientDocMap.containsKey(ingredient)) {
                Toast.makeText(this, "$ingredient is already in the pantry", Toast.LENGTH_SHORT).show()
                binding.autoCompleteIngredient.text.clear()
                return@setOnClickListener
            }

            binding.autoCompleteIngredient.text.clear()

            db.collection("global_pantry")
                .add(mapOf("name" to ingredient))
                .addOnFailureListener {
                    Toast.makeText(this, "Error adding ingredient", Toast.LENGTH_SHORT).show()
                }
        }

        binding.btnFindRecipes.setOnClickListener {
            if (ingredientDocMap.isEmpty()) {
                Toast.makeText(this, "Pantry is empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val query = ingredientDocMap.keys.joinToString(",")
            startActivity(Intent(this, ResultsActivity::class.java).apply {
                putExtra("INGREDIENTS_KEY", query)
            })
        }
    }

    private fun setupDropdown() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            INGREDIENTS
        )
        binding.autoCompleteIngredient.setAdapter(adapter)
        binding.autoCompleteIngredient.threshold = 1
    }

    private fun setupRecyclerView() {
        pantryAdapter = PantryAdapter { docId, name ->
            db.collection("global_pantry").document(docId)
                .delete()
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to remove $name", Toast.LENGTH_SHORT).show()
                }
        }
        binding.rvPantryItems.layoutManager = LinearLayoutManager(this)
        binding.rvPantryItems.adapter = pantryAdapter
    }

    private fun setupFirebaseListener() {
        pantryListener = db.collection("global_pantry")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("Firestore", "Listen failed.", e)
                    return@addSnapshotListener
                }

                ingredientDocMap.clear()
                val items = mutableListOf<PantryItem>()

                for (doc in snapshots!!) {
                    val name = doc.getString("name") ?: continue
                    ingredientDocMap[name] = doc.id
                    items.add(PantryItem(docId = doc.id, name = name))
                }

                pantryAdapter.submitList(items)

                binding.tvEmptyState.visibility =
                    if (items.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        pantryListener?.remove()
    }

    companion object {
        private val INGREDIENTS = arrayOf(
            // Proteins
            "chicken breast", "chicken thighs", "ground beef", "beef steak", "pork chops",
            "bacon", "ham", "sausage", "ground turkey", "lamb", "veal",
            "salmon", "tuna", "shrimp", "cod", "tilapia", "crab", "lobster",
            "tofu", "tempeh", "eggs",
            // Dairy
            "milk", "butter", "heavy cream", "sour cream", "cream cheese",
            "cheddar cheese", "mozzarella", "parmesan", "feta", "greek yogurt",
            // Vegetables
            "onion", "garlic", "tomatoes", "potatoes", "carrots", "broccoli",
            "spinach", "kale", "lettuce", "cabbage", "celery", "cucumber",
            "bell pepper", "zucchini", "eggplant", "corn", "peas", "green beans",
            "asparagus", "mushrooms", "sweet potato", "beets", "cauliflower",
            "brussels sprouts", "artichoke", "leek", "shallots", "scallions",
            // Fruits
            "lemon", "lime", "orange", "apple", "banana", "avocado",
            "tomato", "strawberries", "blueberries", "mango", "pineapple",
            // Grains & Carbs
            "rice", "pasta", "bread", "flour", "oats", "quinoa",
            "breadcrumbs", "tortillas", "noodles", "couscous", "barley",
            // Pantry / Sauces
            "olive oil", "vegetable oil", "soy sauce", "hot sauce", "ketchup",
            "mustard", "mayonnaise", "vinegar", "worcestershire sauce",
            "tomato paste", "coconut milk", "chicken broth", "beef broth",
            "honey", "maple syrup", "sugar", "brown sugar", "salt", "black pepper",
            // Spices
            "cumin", "paprika", "chili powder", "oregano", "thyme", "rosemary",
            "basil", "cinnamon", "turmeric", "ginger", "cayenne", "garlic powder",
            // Legumes
            "black beans", "chickpeas", "lentils", "kidney beans", "pinto beans",
            // Nuts & Seeds
            "almonds", "peanuts", "walnuts", "cashews", "sesame seeds", "peanut butter"
        )
    }
}