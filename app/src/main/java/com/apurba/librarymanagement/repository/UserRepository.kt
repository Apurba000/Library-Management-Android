package com.apurba.librarymanagement.repository

import com.apurba.librarymanagement.models.LoginRequest
import com.apurba.librarymanagement.models.LoginResponse
import com.apurba.librarymanagement.models.RegistrationRequest
import com.apurba.librarymanagement.models.RegistrationResponse
import com.apurba.librarymanagement.models.User
import com.apurba.librarymanagement.models.BorrowRequest
import com.apurba.librarymanagement.models.Loan
import com.apurba.librarymanagement.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository {
    
    private val apiService = RetrofitClient.apiService
    
    suspend fun signIn(username: String, password: String): Result<LoginResponse> = withContext(Dispatchers.IO) {
        try {
            val loginRequest = LoginRequest(username, password)
            val response = apiService.signIn(loginRequest)
            
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    Result.success(loginResponse)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Login failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signUp(username: String, email: String, password: String, confirmPassword: String): Result<RegistrationResponse> = withContext(Dispatchers.IO) {
        try {
            val registrationRequest = RegistrationRequest(username, email, password, confirmPassword)
            val response = apiService.signUp(registrationRequest)
            
            if (response.isSuccessful) {
                response.body()?.let { registrationResponse ->
                    Result.success(registrationResponse)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Registration failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUserProfile(userId: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getUserProfile(userId)
            
            if (response.isSuccessful) {
                response.body()?.let { user ->
                    Result.success(user)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to fetch profile: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createMember(request: com.apurba.librarymanagement.models.CreateMemberRequest): Result<com.apurba.librarymanagement.models.Member> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.createMember(request)
            if (response.isSuccessful) {
                response.body()?.let { member ->
                    Result.success(member)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to create member: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMemberByUserId(userId: String): Result<com.apurba.librarymanagement.models.Member> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getMemberByUserId(userId)
            if (response.isSuccessful) {
                response.body()?.let { member ->
                    Result.success(member)
                } ?: Result.failure(Exception("Empty response body"))
            } else if (response.code() == 404) {
                Result.failure(Exception("Not a member"))
            } else {
                Result.failure(Exception("Failed to fetch member: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun borrowBook(memberId: Int, bookId: Int, borrowedByUserId: Int): Result<Loan> = withContext(Dispatchers.IO) {
        try {
            val borrowRequest = BorrowRequest(memberId, bookId, borrowedByUserId)
            val response = apiService.borrowBook(borrowRequest)
            
            if (response.isSuccessful) {
                response.body()?.let { loan ->
                    Result.success(loan)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to borrow book: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 