package com.apurba.librarymanagement

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apurba.librarymanagement.adapters.BookAdapter
import com.apurba.librarymanagement.models.Book
import com.apurba.librarymanagement.repository.BookRepository
import com.apurba.librarymanagement.utils.SharedPrefs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: BookAdapter
    private lateinit var fabAddBook: FloatingActionButton
    private var allBooks: List<Book> = emptyList()
    private val bookRepository = BookRepository()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Set up toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        
        // Initialize views
        recyclerView = findViewById(R.id.rvBooks)
        fabAddBook = findViewById(R.id.fabAddBook)
        
        // Set up RecyclerView
        setupRecyclerView()
        
        // Load books from backend
        loadBooksFromBackend()
        
        // Set up FAB click listener
        setupFabClickListener()
    }
    
    private fun setupRecyclerView() {
        // Use GridLayoutManager with 2 columns
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        bookAdapter = BookAdapter(
            books = emptyList(),
            onBookClick = { book -> onBookClick(book) }
        )
        recyclerView.adapter = bookAdapter
    }
    
    private fun loadBooksFromBackend() {
        lifecycleScope.launch {
            try {
                val result = bookRepository.getBooks()
                result.fold(
                    onSuccess = { books ->
                        allBooks = books
                        bookAdapter.updateBooks(allBooks)
                        updateEmptyState()
                    },
                    onFailure = { exception ->
                        Toast.makeText(this@MainActivity, "Error: ${exception.message}", Toast.LENGTH_LONG).show()
                        // Load dummy data as fallback
                        loadDummyBooks()
                    }
                )
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Network error: ${e.message}", Toast.LENGTH_LONG).show()
                loadDummyBooks()
            }
        }
    }
    
    private fun loadDummyBooks() {
        allBooks = com.apurba.librarymanagement.utils.DummyData.getDummyBooks()
        bookAdapter.updateBooks(allBooks)
        updateEmptyState()
    }
    
    private fun updateEmptyState() {
        val emptyStateView = findViewById<TextView>(R.id.tvEmptyState)
        emptyStateView?.visibility = if (allBooks.isEmpty()) View.VISIBLE else View.GONE
    }
    
    private fun setupFabClickListener() {
        fabAddBook.setOnClickListener {
            val currentUser = SharedPrefs.getUser(this)
            if (currentUser == null) {
                // User not logged in, go to login
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else if (currentUser.role == 1) {
                // User is admin, launch create book activity
                val intent = Intent(this, CreateBookActivity::class.java)
                startActivityForResult(intent, CREATE_BOOK_REQUEST_CODE)
            } else {
                // User is not admin
                Toast.makeText(this, "This feature is for Admin users only", Toast.LENGTH_LONG).show()
            }
        }
    }
    
    private fun onBookClick(book: Book) {
        // Navigate to book details activity
        val intent = BookDetailsActivity.newIntent(this, book.id)
        startActivity(intent)
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                if (!SharedPrefs.isLoggedIn(this)) {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    // Launch ProfileActivity for logged-in users
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == CREATE_BOOK_REQUEST_CODE && 
            (resultCode == CreateBookActivity.RESULT_BOOK_CREATED || 
             resultCode == CreateBookActivity.RESULT_BOOK_UPDATED)) {
            // Book was created or updated successfully, refresh the book list
            loadBooksFromBackend()
        }
    }
    
    companion object {
        private const val CREATE_BOOK_REQUEST_CODE = 1001
    }
}