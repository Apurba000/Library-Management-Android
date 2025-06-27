package com.apurba.librarymanagement.repository

import com.apurba.librarymanagement.models.Loan
import com.apurba.librarymanagement.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoanRepository {
    
    private val apiService = RetrofitClient.apiService
    
    suspend fun getLoansByMember(memberId: String): Result<List<Loan>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getLoansByMember(memberId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 