package com.apurba.librarymanagement

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.apurba.librarymanagement.models.Book
import com.apurba.librarymanagement.repository.BookRepository
import com.apurba.librarymanagement.utils.SharedPrefs
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class CreateBookActivity : AppCompatActivity() {
    
    private lateinit var etIsbn: TextInputEditText
    private lateinit var etTitle: TextInputEditText
    private lateinit var etAuthor: TextInputEditText
    private lateinit var etPublisher: TextInputEditText
    private lateinit var etPublicationYear: TextInputEditText
    private lateinit var etGenre: TextInputEditText
    private lateinit var etDescription: TextInputEditText
    private lateinit var etTotalCopies: TextInputEditText
    private lateinit var etLocation: TextInputEditText
    private lateinit var etCoverImageUrl: TextInputEditText
    private lateinit var etCategoryId: TextInputEditText
    private lateinit var btnCreateBook: MaterialButton
    private lateinit var btnCancel: MaterialButton
    private lateinit var progressIndicator: CircularProgressIndicator
    
    private val bookRepository = BookRepository()
    private var isEditMode = false
    private var bookId: String = ""
    private var currentBook: Book? = null
    
    companion object {
        const val EXTRA_BOOK_ID = "extra_book_id"
        const val EXTRA_BOOK_DATA = "extra_book_data"
        const val RESULT_BOOK_CREATED = 1001
        const val RESULT_BOOK_UPDATED = 1002
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_book)
        
        // Check if user is logged in and is admin
        val currentUser = SharedPrefs.getUser(this)
        if (currentUser == null || currentUser.role != 1) {
            Toast.makeText(this, "This feature is for Admin users only", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        
        // Check if this is edit mode
        isEditMode = intent.hasExtra(EXTRA_BOOK_ID)
        if (isEditMode) {
            bookId = intent.getStringExtra(EXTRA_BOOK_ID) ?: ""
            currentBook = intent.getParcelableExtra(EXTRA_BOOK_DATA)
            if (bookId.isEmpty() || currentBook == null) {
                Toast.makeText(this, "Invalid book data", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
        }
        
        // Initialize views
        initializeViews()
        
        // Set up toolbar
        setupToolbar()
        
        // Populate form if in edit mode
        if (isEditMode) {
            populateFormWithBookData()
        }
        
        // Set up click listeners
        setupClickListeners()
    }
    
    private fun initializeViews() {
        etIsbn = findViewById(R.id.etIsbn)
        etTitle = findViewById(R.id.etTitle)
        etAuthor = findViewById(R.id.etAuthor)
        etPublisher = findViewById(R.id.etPublisher)
        etPublicationYear = findViewById(R.id.etPublicationYear)
        etGenre = findViewById(R.id.etGenre)
        etDescription = findViewById(R.id.etDescription)
        etTotalCopies = findViewById(R.id.etTotalCopies)
        etLocation = findViewById(R.id.etLocation)
        etCoverImageUrl = findViewById(R.id.etCoverImageUrl)
        etCategoryId = findViewById(R.id.etCategoryId)
        btnCreateBook = findViewById(R.id.btnCreateBook)
        btnCancel = findViewById(R.id.btnCancel)
        progressIndicator = findViewById(R.id.progressIndicator)
    }
    
    private fun setupToolbar() {
        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        
        // Update toolbar title based on mode
        val title = if (isEditMode) "Edit Book" else "Add New Book"
        supportActionBar?.title = title
        
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    
    private fun populateFormWithBookData() {
        currentBook?.let { book ->
            etIsbn.setText(book.isbn)
            etTitle.setText(book.title)
            etAuthor.setText(book.author)
            etPublisher.setText(book.publisher)
            etPublicationYear.setText(book.publicationYear.toString())
            etGenre.setText(book.genre)
            etDescription.setText(book.description)
            etTotalCopies.setText(book.totalCopies.toString())
            etLocation.setText(book.location)
            etCoverImageUrl.setText(book.coverImageUrl ?: "")
            etCategoryId.setText("0") // Default category ID
            
            // Update button text for edit mode
            btnCreateBook.text = "Update Book"
        }
    }
    
    private fun setupClickListeners() {
        btnCreateBook.setOnClickListener {
            if (validateForm()) {
                if (isEditMode) {
                    updateBook()
                } else {
                    createBook()
                }
            }
        }
        
        btnCancel.setOnClickListener {
            finish()
        }
    }
    
    private fun validateForm(): Boolean {
        var isValid = true
        
        // Validate required fields
        if (etIsbn.text.toString().trim().isEmpty()) {
            etIsbn.error = "ISBN is required"
            isValid = false
        }
        
        if (etTitle.text.toString().trim().isEmpty()) {
            etTitle.error = "Title is required"
            isValid = false
        }
        
        if (etAuthor.text.toString().trim().isEmpty()) {
            etAuthor.error = "Author is required"
            isValid = false
        }
        
        if (etPublisher.text.toString().trim().isEmpty()) {
            etPublisher.error = "Publisher is required"
            isValid = false
        }
        
        if (etPublicationYear.text.toString().trim().isEmpty()) {
            etPublicationYear.error = "Publication year is required"
            isValid = false
        } else {
            try {
                val year = etPublicationYear.text.toString().toInt()
                if (year < 1800 || year > 2100) {
                    etPublicationYear.error = "Publication year must be between 1800 and 2100"
                    isValid = false
                }
            } catch (e: NumberFormatException) {
                etPublicationYear.error = "Invalid publication year"
                isValid = false
            }
        }
        
        if (etGenre.text.toString().trim().isEmpty()) {
            etGenre.error = "Genre is required"
            isValid = false
        }
        
        if (etDescription.text.toString().trim().isEmpty()) {
            etDescription.error = "Description is required"
            isValid = false
        }
        
        if (etTotalCopies.text.toString().trim().isEmpty()) {
            etTotalCopies.error = "Total copies is required"
            isValid = false
        } else {
            try {
                val copies = etTotalCopies.text.toString().toInt()
                if (copies <= 0) {
                    etTotalCopies.error = "Total copies must be greater than 0"
                    isValid = false
                }
            } catch (e: NumberFormatException) {
                etTotalCopies.error = "Invalid number of copies"
                isValid = false
            }
        }
        
        if (etLocation.text.toString().trim().isEmpty()) {
            etLocation.error = "Location is required"
            isValid = false
        }
        
        return isValid
    }
    
    private fun createBook() {
        setLoadingState(true)
        
        val isbn = etIsbn.text.toString().trim()
        val title = etTitle.text.toString().trim()
        val author = etAuthor.text.toString().trim()
        val publisher = etPublisher.text.toString().trim()
        val publicationYear = etPublicationYear.text.toString().toInt()
        val genre = etGenre.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val totalCopies = etTotalCopies.text.toString().toInt()
        val location = etLocation.text.toString().trim()
        val coverImageUrl = etCoverImageUrl.text.toString().trim().ifEmpty { "" }
        val categoryId = etCategoryId.text.toString().toIntOrNull() ?: 0
        
        lifecycleScope.launch {
            try {
                val result = bookRepository.createBook(
                    isbn = isbn,
                    title = title,
                    author = author,
                    publisher = publisher,
                    publicationYear = publicationYear,
                    genre = genre,
                    description = description,
                    totalCopies = totalCopies,
                    location = location,
                    coverImageUrl = coverImageUrl,
                    categoryId = categoryId
                )
                
                if (result.isSuccess) {
                    result.getOrNull()?.let { book ->
                        Toast.makeText(this@CreateBookActivity, "Book created successfully!", Toast.LENGTH_LONG).show()
                        setResult(RESULT_BOOK_CREATED)
                        finish()
                    }
                } else {
                    val exception = result.exceptionOrNull()
                    Toast.makeText(this@CreateBookActivity, "Error: ${exception?.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CreateBookActivity, "Network error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                setLoadingState(false)
            }
        }
    }
    
    private fun updateBook() {
        setLoadingState(true)
        
        val isbn = etIsbn.text.toString().trim()
        val title = etTitle.text.toString().trim()
        val author = etAuthor.text.toString().trim()
        val publisher = etPublisher.text.toString().trim()
        val publicationYear = etPublicationYear.text.toString().toInt()
        val genre = etGenre.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val totalCopies = etTotalCopies.text.toString().toInt()
        val location = etLocation.text.toString().trim()
        val coverImageUrl = etCoverImageUrl.text.toString().trim().ifEmpty { "" }
        val categoryId = etCategoryId.text.toString().toIntOrNull() ?: 0
        
        lifecycleScope.launch {
            try {
                val result = bookRepository.updateBook(
                    bookId = bookId,
                    isbn = isbn,
                    title = title,
                    author = author,
                    publisher = publisher,
                    publicationYear = publicationYear,
                    genre = genre,
                    description = description,
                    totalCopies = totalCopies,
                    location = location,
                    coverImageUrl = coverImageUrl,
                    categoryId = categoryId
                )
                
                if (result.isSuccess) {
                    result.getOrNull()?.let { book ->
                        Toast.makeText(this@CreateBookActivity, "Book updated successfully!", Toast.LENGTH_LONG).show()
                        setResult(RESULT_BOOK_UPDATED)
                        finish()
                    }
                } else {
                    val exception = result.exceptionOrNull()
                    Toast.makeText(this@CreateBookActivity, "Error: ${exception?.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CreateBookActivity, "Network error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                setLoadingState(false)
            }
        }
    }
    
    private fun setLoadingState(isLoading: Boolean) {
        progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnCreateBook.isEnabled = !isLoading
        btnCancel.isEnabled = !isLoading
        
        // Disable/enable all input fields
        val inputFields = listOf(
            etIsbn, etTitle, etAuthor, etPublisher, etPublicationYear,
            etGenre, etDescription, etTotalCopies, etLocation, etCoverImageUrl, etCategoryId
        )
        
        inputFields.forEach { field ->
            field.isEnabled = !isLoading
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 