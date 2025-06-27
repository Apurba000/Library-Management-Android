package com.apurba.librarymanagement.models

import com.google.gson.annotations.SerializedName

data class CreateBookRequest(
    val isbn: String,
    val title: String,
    val author: String,
    val publisher: String,
    @SerializedName("publicationYear")
    val publicationYear: Int,
    val genre: String,
    val description: String,
    @SerializedName("totalCopies")
    val totalCopies: Int,
    val location: String,
    @SerializedName("coverImageUrl")
    val coverImageUrl: String,
    @SerializedName("categoryId")
    val categoryId: Int
) 