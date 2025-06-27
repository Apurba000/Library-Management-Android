package com.apurba.librarymanagement

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.apurba.librarymanagement.repository.UserRepository
import com.apurba.librarymanagement.utils.SharedPrefs
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    
    companion object {
        private const val REGISTRATION_REQUEST_CODE = 1001
    }
    
    private lateinit var tilUsername: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var etUsername: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var btnCreateAccount: MaterialButton
    private lateinit var tvForgotPassword: View
    private lateinit var progressIndicator: CircularProgressIndicator
    
    private val userRepository = UserRepository()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        // Initialize views
        initializeViews()
        
        // Set up click listeners
        setupClickListeners()
    }
    
    private fun initializeViews() {
        tilUsername = findViewById(R.id.tilUsername)
        tilPassword = findViewById(R.id.tilPassword)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnCreateAccount = findViewById(R.id.btnCreateAccount)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        progressIndicator = findViewById(R.id.progressIndicator)
    }
    
    private fun setupClickListeners() {
        btnLogin.setOnClickListener {
            performLogin()
        }
        
        btnCreateAccount.setOnClickListener {
            // Navigate to registration activity
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivityForResult(intent, REGISTRATION_REQUEST_CODE)
        }
        
        tvForgotPassword.setOnClickListener {
            // TODO: Navigate to forgot password activity
            Toast.makeText(this, "Forgot password coming soon!", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun performLogin() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()
        
        // Validate input
        if (username.isEmpty()) {
            tilUsername.error = "Username is required"
            return
        }
        
        if (password.isEmpty()) {
            tilPassword.error = "Password is required"
            return
        }
        
        // Clear previous errors
        tilUsername.error = null
        tilPassword.error = null
        
        // Show loading state
        setLoadingState(true)
        
        // Perform login
        lifecycleScope.launch {
            try {
                val result = userRepository.signIn(username, password)
                result.fold(
                    onSuccess = { loginResponse ->
                        // Login successful
                        Toast.makeText(this@LoginActivity, loginResponse.message, Toast.LENGTH_SHORT).show()
                        SharedPrefs.saveUser(this@LoginActivity, loginResponse.user)
                        finish()
                    },
                    onFailure = { exception ->
                        // Login failed
                        Toast.makeText(this@LoginActivity, "Login failed: ${exception.message}", Toast.LENGTH_LONG).show()
                    }
                )
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Network error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                setLoadingState(false)
            }
        }
    }
    
    private fun setLoadingState(isLoading: Boolean) {
        progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnLogin.isEnabled = !isLoading
        btnCreateAccount.isEnabled = !isLoading
        etUsername.isEnabled = !isLoading
        etPassword.isEnabled = !isLoading
    }
    
    private fun navigateToMainActivity() {
        // Go back to MainActivity (which is already in the back stack)
        finish()
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == REGISTRATION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Registration was successful, finish LoginActivity as well
            finish()
        }
    }
} 