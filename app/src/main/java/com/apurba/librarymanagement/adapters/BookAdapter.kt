package com.apurba.librarymanagement.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apurba.librarymanagement.R
import com.apurba.librarymanagement.models.Book
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class BookAdapter(
    private var books: List<Book>,
    private val onBookClick: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coverImage: ImageView = itemView.findViewById(R.id.ivBookCover)
        val titleText: TextView = itemView.findViewById(R.id.tvBookTitle)
        val descriptionText: TextView = itemView.findViewById(R.id.tvBookDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        
        holder.titleText.text = book.title
        holder.descriptionText.text = "By ${book.author} • \nISBN-${book.isbn}"
        
        // Load image using Glide with better error handling
        Glide.with(holder.itemView.context)
            .load(book.imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerCrop()
            .into(holder.coverImage)
        
        // Set click listener for the entire item
        holder.itemView.setOnClickListener { onBookClick(book) }
    }

    fun updateBooks(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = books.size
} 