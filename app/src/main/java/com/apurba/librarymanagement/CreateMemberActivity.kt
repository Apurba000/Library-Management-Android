package com.apurba.librarymanagement

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.apurba.librarymanagement.models.CreateMemberRequest
import com.apurba.librarymanagement.repository.UserRepository
import com.apurba.librarymanagement.utils.SharedPrefs
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CreateMemberActivity : AppCompatActivity() {
    private lateinit var etFirstName: TextInputEditText
    private lateinit var etLastName: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var etAddress: TextInputEditText
    private lateinit var etDateOfBirth: TextInputEditText
    private lateinit var btnCreateMember: MaterialButton
    private lateinit var btnCancel: MaterialButton
    private lateinit var progressIndicator: CircularProgressIndicator

    private val userRepository = UserRepository()
    private var selectedDate: Calendar? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_member)

        // Initialize views
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etPhone = findViewById(R.id.etPhone)
        etAddress = findViewById(R.id.etAddress)
        etDateOfBirth = findViewById(R.id.etDateOfBirth)
        btnCreateMember = findViewById(R.id.btnCreateMember)
        btnCancel = findViewById(R.id.btnCancel)
        progressIndicator = findViewById(R.id.progressIndicator)

        setupToolbar()
        setupClickListeners()
        setupDatePicker()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupClickListeners() {
        btnCreateMember.setOnClickListener {
            if (validateForm()) {
                createMember()
            }
        }
        btnCancel.setOnClickListener { finish() }
    }

    private fun setupDatePicker() {
        etDateOfBirth.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                etDateOfBirth.setText(dateFormat.format(selectedDate?.time ?: Date()))
            },
            year,
            month,
            day
        )

        // Set max date to today (user can't be born in the future)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        
        // Set min date to reasonable past date (e.g., 120 years ago)
        val minDate = Calendar.getInstance()
        minDate.add(Calendar.YEAR, -120)
        datePickerDialog.datePicker.minDate = minDate.timeInMillis

        datePickerDialog.show()
    }

    private fun validateForm(): Boolean {
        var isValid = true
        if (etFirstName.text.isNullOrBlank()) {
            etFirstName.error = "First name required"
            isValid = false
        }
        if (etLastName.text.isNullOrBlank()) {
            etLastName.error = "Last name required"
            isValid = false
        }
        if (etPhone.text.isNullOrBlank()) {
            etPhone.error = "Phone required"
            isValid = false
        }
        if (etAddress.text.isNullOrBlank()) {
            etAddress.error = "Address required"
            isValid = false
        }
        if (selectedDate == null) {
            etDateOfBirth.error = "Date of birth required"
            isValid = false
        }
        return isValid
    }

    private fun createMember() {
        setLoadingState(true)
        val user = SharedPrefs.getUser(this)
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            setLoadingState(false)
            return
        }

        // Format date to ISO 8601 format
        val isoDateString = selectedDate?.let { calendar ->
            isoDateFormat.format(calendar.time)
        } ?: ""

        val request = CreateMemberRequest(
            firstName = etFirstName.text.toString().trim(),
            lastName = etLastName.text.toString().trim(),
            phone = etPhone.text.toString().trim(),
            address = etAddress.text.toString().trim(),
            dateOfBirth = isoDateString,
            userId = user.id
        )
        lifecycleScope.launch {
            try {
                val result = userRepository.createMember(request)
                if (result.isSuccess) {
                    Toast.makeText(this@CreateMemberActivity, "Membership created!", Toast.LENGTH_LONG).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    val exception = result.exceptionOrNull()
                    Toast.makeText(this@CreateMemberActivity, "Error: ${exception?.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CreateMemberActivity, "Network error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                setLoadingState(false)
            }
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnCreateMember.isEnabled = !isLoading
        btnCancel.isEnabled = !isLoading
        etFirstName.isEnabled = !isLoading
        etLastName.isEnabled = !isLoading
        etPhone.isEnabled = !isLoading
        etAddress.isEnabled = !isLoading
        etDateOfBirth.isEnabled = !isLoading
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
} 