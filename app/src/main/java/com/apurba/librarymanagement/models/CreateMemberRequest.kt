package com.apurba.librarymanagement.models

import com.google.gson.annotations.SerializedName

data class CreateMemberRequest(
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    val phone: String,
    val address: String,
    @SerializedName("dateOfBirth")
    val dateOfBirth: String,
    @SerializedName("userId")
    val userId: Int
) 