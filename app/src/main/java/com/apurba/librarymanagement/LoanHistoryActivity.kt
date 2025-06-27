package com.apurba.librarymanagement

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apurba.librarymanagement.adapters.LoanAdapter
import com.apurba.librarymanagement.models.Loan
import com.apurba.librarymanagement.models.Member
import com.apurba.librarymanagement.repository.LoanRepository
import com.apurba.librarymanagement.repository.UserRepository
import com.apurba.librarymanagement.utils.SharedPrefs
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.launch

class LoanHistoryActivity : AppCompatActivity() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmptyState: LinearLayout
    private lateinit var progressIndicator: CircularProgressIndicator
    private lateinit var loanAdapter: LoanAdapter
    
    private val loanRepository = LoanRepository()
    private val userRepository = UserRepository()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_history)
        
        // Initialize views
        initializeViews()
        
        // Set up toolbar
        setupToolbar()
        
        // Set up RecyclerView
        setupRecyclerView()
        
        // Load loan history
        loadLoanHistory()
    }
    
    private fun initializeViews() {
        recyclerView = findViewById(R.id.recyclerView)
        tvEmptyState = findViewById(R.id.tvEmptyState)
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
    
    private fun setupRecyclerView() {
        loanAdapter = LoanAdapter(emptyList()) { loan ->
            // Handle loan click - could navigate to loan details or book details
            Toast.makeText(this, "Loan #${loan.id} clicked", Toast.LENGTH_SHORT).show()
        }
        
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@LoanHistoryActivity)
            adapter = loanAdapter
        }
    }
    
    private fun loadLoanHistory() {
        setLoadingState(true)
        
        // Get current user
        val currentUser = SharedPrefs.getUser(this)
        if (currentUser == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        lifecycleScope.launch {
            try {
                // First get the member ID for the current user
                val memberResult = userRepository.getMemberByUserId(currentUser.id.toString())
                if (memberResult.isSuccess) {
                    val member = memberResult.getOrNull()
                    if (member != null) {
                        // Now get loans for this member
                        val loansResult = loanRepository.getLoansByMember(member.id.toString())
                        if (loansResult.isSuccess) {
                            val loans = loansResult.getOrNull() ?: emptyList()
                            displayLoans(loans)
                        } else {
                            val exception = loansResult.exceptionOrNull()
                            Toast.makeText(this@LoanHistoryActivity, "Error loading loans: ${exception?.message}", Toast.LENGTH_LONG).show()
                            showEmptyState()
                        }
                    } else {
                        Toast.makeText(this@LoanHistoryActivity, "Member profile not found", Toast.LENGTH_LONG).show()
                        showEmptyState()
                    }
                } else {
                    val exception = memberResult.exceptionOrNull()
                    Toast.makeText(this@LoanHistoryActivity, "Error loading member profile: ${exception?.message}", Toast.LENGTH_LONG).show()
                    showEmptyState()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoanHistoryActivity, "Network error: ${e.message}", Toast.LENGTH_LONG).show()
                showEmptyState()
            } finally {
                setLoadingState(false)
            }
        }
    }
    
    private fun displayLoans(loans: List<Loan>) {
        if (loans.isEmpty()) {
            showEmptyState()
        } else {
            hideEmptyState()
            loanAdapter.updateLoans(loans)
        }
    }
    
    private fun showEmptyState() {
        tvEmptyState.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }
    
    private fun hideEmptyState() {
        tvEmptyState.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }
    
    private fun setLoadingState(isLoading: Boolean) {
        progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            recyclerView.visibility = View.GONE
            tvEmptyState.visibility = View.GONE
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 