package com.apurba.librarymanagement.models

import com.google.gson.annotations.SerializedName

data class BorrowRequest(
    @SerializedName("memberId")
    val memberId: Int,
    @SerializedName("bookId")
    val bookId: Int,
    @SerializedName("borrowedByUserId")
    val borrowedByUserId: Int
) 