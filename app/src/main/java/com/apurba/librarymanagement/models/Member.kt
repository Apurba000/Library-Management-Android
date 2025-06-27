package com.apurba.librarymanagement.models

import com.google.gson.annotations.SerializedName

data class Member(
    val id: Int,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    val phone: String,
    val address: String,
    @SerializedName("dateOfBirth")
    val dateOfBirth: String,
    @SerializedName("membershipStatus")
    val membershipStatus: Int,
    @SerializedName("isActive")
    val isActive: Boolean,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("activeLoansCount")
    val activeLoansCount: Int
) {
    val fullName: String
        get() = "$firstName $lastName"
        
    val membershipStatusText: String
        get() = when (membershipStatus) {
            0 -> "Active"
            1 -> "Suspended"
            2 -> "Expired"
            else -> "Unknown"
        }
} 