package com.apurba.librarymanagement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.apurba.librarymanagement.models.Book
import com.apurba.librarymanagement.repository.BookRepository
import com.apurba.librarymanagement.utils.SharedPrefs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class BookDetailsActivity : AppCompatActivity() {
    
    private lateinit var ivBookCover: ImageView
    private lateinit var tvBookTitle: TextView
    private lateinit var tvBookAuthor: TextView
    private lateinit var tvPublicationYear: TextView
    private lateinit var tvIsbn: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvDescription: TextView
    private lateinit var btnBorrowBook: MaterialButton
    private lateinit var btnEditBook: MaterialButton
    
    private val bookRepository = BookRepository()
    private var bookId: Int = 0
    private var currentBook: Book? = null
    
    companion object {
        private const val EXTRA_BOOK_ID = "extra_book_id"
        private const val EDIT_BOOK_REQUEST_CODE = 2001
        private const val CREATE_MEMBERSHIP_REQUEST_CODE = 2002
        
        fun newIntent(context: Context, bookId: Int): Intent {
            return Intent(context, BookDetailsActivity::class.java).apply {
                putExtra(EXTRA_BOOK_ID, bookId)
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)
        
        // Get book ID from intent
        bookId = intent.getIntExtra(EXTRA_BOOK_ID, 0)
        if (bookId == 0) {
            Toast.makeText(this, "Invalid book ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        // Initialize views
        initializeViews()
        
        // Set up toolbar
        setupToolbar()
        
        // Load book details
        loadBookDetails()
        
        // Set up click listeners
        setupClickListeners()
    }
    
    private fun initializeViews() {
        ivBookCover = findViewById(R.id.ivBookCover)
        tvBookTitle = findViewById(R.id.tvBookTitle)
        tvBookAuthor = findViewById(R.id.tvBookAuthor)
        tvPublicationYear = findViewById(R.id.tvPublicationYear)
        tvIsbn = findViewById(R.id.tvIsbn)
        tvLocation = findViewById(R.id.tvLocation)
        tvStatus = findViewById(R.id.tvStatus)
        tvDescription = findViewById(R.id.tvDescription)
        btnBorrowBook = findViewById(R.id.btnBorrowBook)
        btnEditBook = findViewById(R.id.btnEditBook)
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
    
    private fun loadBookDetails() {
        lifecycleScope.launch {
            try {
                val result = bookRepository.getBookById(bookId.toString())
                result.fold(
                    onSuccess = { book ->
                        displayBookDetails(book)
                    },
                    onFailure = { exception ->
                        Toast.makeText(this@BookDetailsActivity, "Error: ${exception.message}", Toast.LENGTH_LONG).show()
                        finish()
                    }
                )
            } catch (e: Exception) {
                Toast.makeText(this@BookDetailsActivity, "Network error: ${e.message}", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
    
    private fun displayBookDetails(book: Book) {
        // Store the current book for edit functionality
        currentBook = book
        
        // Set title and author
        tvBookTitle.text = "📖 ${book.title}"
        tvBookAuthor.text = "by ${book.author}"
        
        // Set publication year
        book.publicationYear?.let { year ->
            tvPublicationYear.text = "📅 Published: $year"
        } ?: run {
            tvPublicationYear.text = "📅 Published: Unknown"
        }
        
        // Set ISBN
        tvIsbn.text = "📚 ISBN: ${book.isbn}"
        
        // Set location
        book.location?.let { location ->
            tvLocation.text = "📍 Location: $location"
        } ?: run {
            tvLocation.text = "📍 Location: Not specified"
        }
        
        // Set status
        tvStatus.text = "📊 Status: ${book.availabilityText}"
        
        // Set description
        book.description?.let { description ->
            tvDescription.text = description
        } ?: run {
            tvDescription.text = "No description available."
        }
        
        // Load book cover image
        if (book.imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(book.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(ivBookCover)
        }
        
        // Set borrow button state
        if (book.isAvailable) {
            btnBorrowBook.text = "BORROW BOOK"
            btnBorrowBook.isEnabled = true
        } else {
            btnBorrowBook.text = "UNAVAILABLE"
            btnBorrowBook.isEnabled = false
        }
    }
    
    private fun setupClickListeners() {
        btnBorrowBook.setOnClickListener {
            checkMembershipAndBorrow()
        }
        
        btnEditBook.setOnClickListener {
            // Check if user is logged in and is admin
            val currentUser = com.apurba.librarymanagement.utils.SharedPrefs.getUser(this)
            if (currentUser == null || currentUser.role != 1) {
                Toast.makeText(this, "This feature is for Admin users only", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            
            // Launch CreateBookActivity in edit mode
            val intent = Intent(this, CreateBookActivity::class.java).apply {
                putExtra(CreateBookActivity.EXTRA_BOOK_ID, bookId.toString())
                putExtra(CreateBookActivity.EXTRA_BOOK_DATA, currentBook)
            }
            startActivityForResult(intent, EDIT_BOOK_REQUEST_CODE)
        }
    }
    
    private fun checkMembershipAndBorrow() {
        val currentUser = com.apurba.librarymanagement.utils.SharedPrefs.getUser(this)
        if (currentUser == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Set loading state
        setBorrowLoadingState(true)
        
        lifecycleScope.launch {
            try {
                val userRepository = com.apurba.librarymanagement.repository.UserRepository()
                val result = userRepository.getMemberByUserId(currentUser.id.toString())
                
                if (result.isSuccess) {
                    // User is a member, proceed with borrowing
                    val member = result.getOrNull()
                    if (member != null) {
                        SharedPrefs.saveMember(this@BookDetailsActivity, member.id)
                        performBorrow(member.id, currentUser.id)
                    } else {
                        Toast.makeText(this@BookDetailsActivity, 
                            "Member data not found", 
                            Toast.LENGTH_LONG).show()
                        setBorrowLoadingState(false)
                    }
                } else {
                    val exception = result.exceptionOrNull()
                    if (exception?.message == "Not a member") {
                        // User is not a member, launch membership creation
                        setBorrowLoadingState(false)
                        val intent = Intent(this@BookDetailsActivity, CreateMemberActivity::class.java)
                        startActivityForResult(intent, CREATE_MEMBERSHIP_REQUEST_CODE)
                    } else {
                        Toast.makeText(this@BookDetailsActivity, 
                            "Error checking membership: ${exception?.message}", 
                            Toast.LENGTH_LONG).show()
                        setBorrowLoadingState(false)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@BookDetailsActivity, 
                    "Network error: ${e.message}", 
                    Toast.LENGTH_LONG).show()
                setBorrowLoadingState(false)
            }
        }
    }
    
    private fun performBorrow(memberId: Int, userId: Int) {
        lifecycleScope.launch {
            try {
                val userRepository = com.apurba.librarymanagement.repository.UserRepository()
                val result = userRepository.borrowBook(memberId, bookId, userId)
                
                if (result.isSuccess) {
                    val loan = result.getOrNull()
                    if (loan != null) {
                        // Show success message with loan details
                        val dueDate = formatDate(loan.dueDate)
                        Toast.makeText(this@BookDetailsActivity, 
                            "Book borrowed successfully! Due date: $dueDate", 
                            Toast.LENGTH_LONG).show()
                        
                        // Decrement book availability
                        decrementBookAvailability()
                    } else {
                        Toast.makeText(this@BookDetailsActivity, 
                            "Borrow successful but no loan details received", 
                            Toast.LENGTH_LONG).show()
                        setBorrowLoadingState(false)
                    }
                } else {
                    val exception = result.exceptionOrNull()
                    Toast.makeText(this@BookDetailsActivity, 
                        "Failed to borrow book: ${exception?.message}", 
                        Toast.LENGTH_LONG).show()
                    setBorrowLoadingState(false)
                }
            } catch (e: Exception) {
                Toast.makeText(this@BookDetailsActivity, 
                    "Network error during borrow: ${e.message}", 
                    Toast.LENGTH_LONG).show()
                setBorrowLoadingState(false)
            }
        }
    }
    
    private fun decrementBookAvailability() {
        lifecycleScope.launch {
            try {
                val result = bookRepository.decrementBookAvailability(bookId.toString())
                
                if (result.isSuccess) {
                    // Availability decremented successfully, now refresh book details
                    loadBookDetails()
                } else {
                    val exception = result.exceptionOrNull()
                    Toast.makeText(this@BookDetailsActivity, 
                        "Book borrowed but failed to update availability: ${exception?.message}", 
                        Toast.LENGTH_LONG).show()
                    // Still refresh book details to show current state
                    loadBookDetails()
                }
            } catch (e: Exception) {
                Toast.makeText(this@BookDetailsActivity, 
                    "Error updating book availability: ${e.message}", 
                    Toast.LENGTH_LONG).show()
                // Still refresh book details to show current state
                loadBookDetails()
            }
        }
    }
    
    private fun setBorrowLoadingState(isLoading: Boolean) {
        btnBorrowBook.isEnabled = !isLoading
        if (isLoading) {
            btnBorrowBook.text = "BORROWING..."
        } else {
            // Reset button text based on book availability
            currentBook?.let { book ->
                if (book.isAvailable) {
                    btnBorrowBook.text = "BORROW BOOK"
                } else {
                    btnBorrowBook.text = "UNAVAILABLE"
                }
            }
        }
    }
    
    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.getDefault())
            val outputFormat = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
            val date = inputFormat.parse(dateString)
            outputFormat.format(date ?: java.util.Date())
        } catch (e: Exception) {
            dateString
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == EDIT_BOOK_REQUEST_CODE && resultCode == CreateBookActivity.RESULT_BOOK_UPDATED) {
            // Book was updated successfully, refresh the book details
            loadBookDetails()
        } else if (requestCode == CREATE_MEMBERSHIP_REQUEST_CODE && resultCode == RESULT_OK) {
            // Membership was created successfully, automatically proceed with borrowing
            Toast.makeText(this, "Membership created successfully! Proceeding with borrow...", Toast.LENGTH_LONG).show()
            
            // Wait a moment for the membership to be fully created, then try borrowing
            lifecycleScope.launch {
                kotlinx.coroutines.delay(1000) // Small delay to ensure membership is created
                checkMembershipAndBorrow()
            }
        }
    }
} 