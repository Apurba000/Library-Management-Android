package com.apurba.librarymanagement.network

import com.apurba.librarymanagement.models.Book
import com.apurba.librarymanagement.models.CreateBookRequest
import com.apurba.librarymanagement.models.LoginRequest
import com.apurba.librarymanagement.models.LoginResponse
import com.apurba.librarymanagement.models.RegistrationRequest
import com.apurba.librarymanagement.models.RegistrationResponse
import com.apurba.librarymanagement.models.User
import com.apurba.librarymanagement.models.CreateMemberRequest
import com.apurba.librarymanagement.models.Member
import com.apurba.librarymanagement.models.BorrowRequest
import com.apurba.librarymanagement.models.Loan
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.PATCH

interface ApiService {
    
    @GET("api/books")
    suspend fun getBooks(): Response<List<Book>>
    
    @GET("api/books")
    suspend fun searchBooks(@Query("search") searchTerm: String): Response<List<Book>>
    
    @GET("api/books/{id}")
    suspend fun getBookById(@Path("id") bookId: String): Response<Book>
    
    @POST("api/Users/signin")
    suspend fun signIn(@Body loginRequest: LoginRequest): Response<LoginResponse>
    
    @POST("api/Users/signup")
    suspend fun signUp(@Body registrationRequest: RegistrationRequest): Response<RegistrationResponse>
    
    @GET("api/Users/{id}")
    suspend fun getUserProfile(@Path("id") userId: String): Response<User>
    
    @POST("api/Books")
    suspend fun createBook(@Body createBookRequest: CreateBookRequest): Response<Book>
    
    @PUT("api/Books/{id}")
    suspend fun updateBook(@Path("id") bookId: String, @Body createBookRequest: CreateBookRequest): Response<Book>
    
    @PATCH("api/Books/{id}/decrement-availability")
    suspend fun decrementBookAvailability(@Path("id") bookId: String): Response<Book>
    
    @POST("api/Members")
    suspend fun createMember(@Body createMemberRequest: CreateMemberRequest): Response<Member>
    
    @GET("api/Members/by-user/{userId}")
    suspend fun getMemberByUserId(@Path("userId") userId: String): Response<Member>
    
    @POST("api/Loans/borrow")
    suspend fun borrowBook(@Body borrowRequest: BorrowRequest): Response<Loan>
    
    @GET("api/Loans/by-member/{memberId}")
    suspend fun getLoansByMember(@Path("memberId") memberId: String): Response<List<Loan>>
} 