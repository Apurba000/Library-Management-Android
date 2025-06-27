package com.apurba.librarymanagement.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val id: Int,
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
    @SerializedName("availableCopies")
    val availableCopies: Int,
    val location: String,
    @SerializedName("coverImageUrl")
    val coverImageUrl: String?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?
) : Parcelable {
    val isAvailable: Boolean
        get() = availableCopies > 0
    
    val availabilityText: String
        get() = if (isAvailable) {
            if (availableCopies == 1) "Available: 1 copy" else "Available: $availableCopies copies"
        } else "Unavailable"
    
    val imageUrl: String
        get() = coverImageUrl ?: ""
} 