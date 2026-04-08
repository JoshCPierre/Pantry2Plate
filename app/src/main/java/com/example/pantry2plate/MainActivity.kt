package com.example.pantry2plate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find your temporary button
        val btnTempNav = findViewById<Button>(R.id.btnTempNav)

        // Set up the click listener
        btnTempNav.setOnClickListener {
            // Launch your InputActivity!
            val intent = Intent(this, InputActivity::class.java)
            startActivity(intent)
        }
    }
}