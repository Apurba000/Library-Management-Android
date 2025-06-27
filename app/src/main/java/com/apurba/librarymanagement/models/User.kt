package com.apurba.librarymanagement.models

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val role: Int,
    @SerializedName("lastLoginDate")
    val lastLoginDate: String?,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null,
    @SerializedName("isActive")
    val isActive: Boolean? = null
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    val user: User
)

data class RegistrationRequest(
    val username: String,
    val email: String,
    val password: String,
    @SerializedName("confirmPassword")
    val confirmPassword: String
)

data class RegistrationResponse(
    val message: String,
    val user: User
) 