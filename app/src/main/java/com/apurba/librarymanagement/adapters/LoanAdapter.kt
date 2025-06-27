package com.apurba.librarymanagement.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apurba.librarymanagement.R
import com.apurba.librarymanagement.models.Loan
import java.text.SimpleDateFormat
import java.util.*

class LoanAdapter(
    private var loans: List<Loan>,
    private val onLoanClick: (Loan) -> Unit
) : RecyclerView.Adapter<LoanAdapter.LoanViewHolder>() {

    class LoanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvLoanId: TextView = itemView.findViewById(R.id.tvLoanId)
        val tvBookId: TextView = itemView.findViewById(R.id.tvBookId)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvLoanDate: TextView = itemView.findViewById(R.id.tvLoanDate)
        val tvDueDate: TextView = itemView.findViewById(R.id.tvDueDate)
        val tvReturnDate: TextView = itemView.findViewById(R.id.tvReturnDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_loan, parent, false)
        return LoanViewHolder(view)
    }

    override fun onBindViewHolder(holder: LoanViewHolder, position: Int) {
        val loan = loans[position]
        
        holder.tvLoanId.text = "Loan #${loan.id}"
        holder.tvBookId.text = "Book ID: ${loan.bookId}"
        
        // Set status with color
        val statusText = when (loan.status) {
            0 -> "🟡 Active"
            1 -> "🟢 Returned"
            2 -> "🔴 Overdue"
            else -> "❓ Unknown"
        }
        holder.tvStatus.text = statusText
        
        // Format dates
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val apiDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'", Locale.getDefault())
        
        try {
            val loanDate = apiDateFormat.parse(loan.loanDate)
            holder.tvLoanDate.text = "📅 Loaned: ${dateFormat.format(loanDate ?: Date())}"
            
            val dueDate = apiDateFormat.parse(loan.dueDate)
            holder.tvDueDate.text = "⏰ Due: ${dateFormat.format(dueDate ?: Date())}"
            
            loan.returnDate?.let { returnDateStr ->
                val returnDate = apiDateFormat.parse(returnDateStr)
                holder.tvReturnDate.text = "✅ Returned: ${dateFormat.format(returnDate ?: Date())}"
                holder.tvReturnDate.visibility = View.VISIBLE
            } ?: run {
                holder.tvReturnDate.visibility = View.GONE
            }
        } catch (e: Exception) {
            holder.tvLoanDate.text = "📅 Loaned: Unknown"
            holder.tvDueDate.text = "⏰ Due: Unknown"
            holder.tvReturnDate.visibility = View.GONE
        }
        
        holder.itemView.setOnClickListener {
            onLoanClick(loan)
        }
    }

    override fun getItemCount(): Int = loans.size

    fun updateLoans(newLoans: List<Loan>) {
        loans = newLoans
        notifyDataSetChanged()
    }
} 