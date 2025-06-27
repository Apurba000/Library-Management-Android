package com.apurba.librarymanagement

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.apurba.librarymanagement.repository.UserRepository
import com.apurba.librarymanagement.utils.SharedPrefs
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {
    
    private lateinit var tvUsername: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvRole: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvUserId: TextView
    private lateinit var tvCreatedAt: TextView
    private lateinit var tvUpdatedAt: TextView
    private lateinit var btnEditProfile: MaterialButton
    private lateinit var btnLoanHistory: MaterialButton
    private lateinit var btnLogout: MaterialButton
    private lateinit var progressIndicator: CircularProgressIndicator
    
    private val userRepository = UserRepository()
    private var userId: String = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        
        // Get user ID from SharedPreferences
        val currentUser = SharedPrefs.getUser(this)
        if (currentUser == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        userId = currentUser.id.toString()
        
        // Initialize views
        initializeViews()
        
        // Set up toolbar
        setupToolbar()
        
        // Load user profile
        loadUserProfile()
        
        // Set up click listeners
        setupClickListeners()
    }
    
    private fun initializeViews() {
        tvUsername = findViewById(R.id.tvUsername)
        tvEmail = findViewById(R.id.tvEmail)
        tvRole = findViewById(R.id.tvRole)
        tvStatus = findViewById(R.id.tvStatus)
        tvUserId = findViewById(R.id.tvUserId)
        tvCreatedAt = findViewById(R.id.tvCreatedAt)
        tvUpdatedAt = findViewById(R.id.tvUpdatedAt)
        btnEditProfile = findViewById(R.id.btnEditProfile)
        btnLoanHistory = findViewById(R.id.btnLoanHistory)
        btnLogout = findViewById(R.id.btnLogout)
        progressIndicator = findViewById(R.id.progressIndicator)
    }
    
    private fun setupToolbar() {
        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    
    private fun loadUserProfile() {
        setLoadingState(true)
        
        lifecycleScope.launch {
            try {
                val result = userRepository.getUserProfile(userId)
                if (result.isSuccess) {
                    result.getOrNull()?.let { user ->
                        displayUserProfile(user)
                    }
                } else {
                    val exception = result.exceptionOrNull()
                    Toast.makeText(this@ProfileActivity, "Error: ${exception?.message}", Toast.LENGTH_LONG).show()
                    // Fallback to cached user data
                    val cachedUser = SharedPrefs.getUser(this@ProfileActivity)
                    cachedUser?.let { displayUserProfile(it) }
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Network error: ${e.message}", Toast.LENGTH_LONG).show()
                // Fallback to cached user data
                val cachedUser = SharedPrefs.getUser(this@ProfileActivity)
                cachedUser?.let { displayUserProfile(it) }
            } finally {
                setLoadingState(false)
            }
        }
    }
    
    private fun displayUserProfile(user: com.apurba.librarymanagement.models.User) {
        // Display user information
        tvUsername.text = "👤 ${user.username}"
        tvEmail.text = "📧 ${user.email}"
        tvUserId.text = user.id.toString()
        
        // Display role
        val roleText = when (user.role) {
            0 -> "Member"
            1 -> "Admin"
            else -> "Unknown"
        }
        tvRole.text = "🏷️ Role: $roleText"
        
        // Display status
        val statusText = if (user.isActive == true) "✅ Active" else "❌ Inactive"
        tvStatus.text = statusText
        
        // Display dates
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        
        user.createdAt?.let { createdAt ->
            try {
                val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'", Locale.getDefault()).parse(createdAt)
                tvCreatedAt.text = dateFormat.format(date ?: Date())
            } catch (e: Exception) {
                tvCreatedAt.text = "Unknown"
            }
        } ?: run {
            tvCreatedAt.text = "Unknown"
        }
        
        user.updatedAt?.let { updatedAt ->
            try {
                val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'", Locale.getDefault()).parse(updatedAt)
                tvUpdatedAt.text = dateFormat.format(date ?: Date())
            } catch (e: Exception) {
                tvUpdatedAt.text = "Unknown"
            }
        } ?: run {
            tvUpdatedAt.text = "Unknown"
        }
    }
    
    private fun setupClickListeners() {
        btnEditProfile.setOnClickListener {
            // TODO: Navigate to edit profile activity
            Toast.makeText(this, "Edit profile coming soon!", Toast.LENGTH_SHORT).show()
        }
        
        btnLoanHistory.setOnClickListener {
            val intent = Intent(this, LoanHistoryActivity::class.java)
            startActivity(intent)
        }
        
        btnLogout.setOnClickListener {
            performLogout()
        }
    }
    
    private fun performLogout() {
        // Clear user data from SharedPreferences
        SharedPrefs.clearUser(this)
        
        // Show logout message
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
        
        // Navigate back to MainActivity
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
    
    private fun setLoadingState(isLoading: Boolean) {
        progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnEditProfile.isEnabled = !isLoading
        btnLoanHistory.isEnabled = !isLoading
        btnLogout.isEnabled = !isLoading
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 