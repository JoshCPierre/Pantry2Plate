package com.example.pantry2plate

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pantry2plate.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.registerSubmitButton.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val email = binding.emailET.text.toString().trim()
        val password = binding.passwordET.text.toString().trim()
        val confirmPassword = binding.confirmPasswordET.text.toString().trim()

        if (email.isEmpty()) {
            binding.emailET.error = "Enter email"
            binding.emailET.requestFocus()
            return
        }

        if (password.isEmpty()) {
            binding.passwordET.error = "Enter password"
            binding.passwordET.requestFocus()
            return
        }

        if (confirmPassword.isEmpty()) {
            binding.confirmPasswordET.error = "Confirm password"
            binding.confirmPasswordET.requestFocus()
            return
        }

        if (password != confirmPassword) {
            binding.confirmPasswordET.error = "Passwords do not match"
            binding.confirmPasswordET.requestFocus()
            return
        }

        if (password.length < 6) {
            binding.passwordET.error = "Password must be at least 6 characters"
            binding.passwordET.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Registration successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish() // go back to login screen
                } else {
                    Toast.makeText(
                        this,
                        task.exception?.message ?: "Registration failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}