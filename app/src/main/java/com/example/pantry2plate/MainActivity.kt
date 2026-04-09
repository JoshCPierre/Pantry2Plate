package com.example.pantry2plate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the buttons from Klarissa's layout
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)

        // 1. Send them to the actual Login screen you just built
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // 2. Placeholder for when you build the Register screen later
        registerButton.setOnClickListener {
             val intent = Intent(this, RegisterActivity::class.java)
             startActivity(intent)
        }
    }
}