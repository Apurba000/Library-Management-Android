package com.apurba.librarymanagement.repository

import com.apurba.librarymanagement.models.Book
import com.apurba.librarymanagement.models.CreateBookRequest
import com.apurba.librarymanagement.network.RetrofitClient
import com.apurba.librarymanagement.utils.DummyData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookRepository {
    
    private val apiService = RetrofitClient.apiService
    
    suspend fun getBooks(): Result<List<Book>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getBooks()
            
            if (response.isSuccessful) {
                response.body()?.let { books ->
                    Result.success(books)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to fetch books: ${response.code()}"))
            }
        } catch (e: Exception) {
            // Fallback to dummy data if network fails
            Result.success(DummyData.getDummyBooks())
        }
    }
    
    suspend fun searchBooks(query: String): Result<List<Book>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.searchBooks(query)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to search books: ${response.code()}"))
            }
        } catch (e: Exception) {
            // Fallback to dummy data if network fails
            val allBooks = DummyData.getDummyBooks()
            val filteredBooks = allBooks.filter { book ->
                book.title.contains(query, ignoreCase = true) ||
                book.author.contains(query, ignoreCase = true) ||
                book.isbn.contains(query, ignoreCase = true)
            }
            Result.success(filteredBooks)
        }
    }
    
    suspend fun getBookById(bookId: String): Result<Book> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getBookById(bookId)
            if (response.isSuccessful) {
                response.body()?.let { book ->
                    Result.success(book)
                } ?: Result.failure(Exception("Book not found"))
            } else {
                Result.failure(Exception("Failed to fetch book: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createBook(
        isbn: String,
        title: String,
        author: String,
        publisher: String,
        publicationYear: Int,
        genre: String,
        description: String,
        totalCopies: Int,
        location: String,
        coverImageUrl: String,
        categoryId: Int
    ): Result<Book> = withContext(Dispatchers.IO) {
        try {
            val createBookRequest = CreateBookRequest(
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
            
            val response = apiService.createBook(createBookRequest)
            
            if (response.isSuccessful) {
                response.body()?.let { book ->
                    Result.success(book)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to create book: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateBook(
        bookId: String,
        isbn: String,
        title: String,
        author: String,
        publisher: String,
        publicationYear: Int,
        genre: String,
        description: String,
        totalCopies: Int,
        location: String,
        coverImageUrl: String,
        categoryId: Int
    ): Result<Book> = withContext(Dispatchers.IO) {
        try {
            val createBookRequest = CreateBookRequest(
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
            
            val response = apiService.updateBook(bookId, createBookRequest)
            
            if (response.isSuccessful) {
                response.body()?.let { book ->
                    Result.success(book)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to update book: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun decrementBookAvailability(bookId: String): Result<Book> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.decrementBookAvailability(bookId)
            
            if (response.isSuccessful) {
                response.body()?.let { book ->
                    Result.success(book)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to decrement book availability: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 