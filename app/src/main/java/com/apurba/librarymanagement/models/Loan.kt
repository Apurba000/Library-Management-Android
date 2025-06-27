package com.apurba.librarymanagement.models

import com.google.gson.annotations.SerializedName

data class Loan(
    val id: Int,
    val memberId: Int,
    val bookId: Int,
    val status: Int,
    val loanDate: String,
    val dueDate: String,
    val returnDate: String?,
    val createdAt: String,
    val updatedAt: String,
    val member: Member?,
    val book: Book? = null // We'll populate this separately if needed
)