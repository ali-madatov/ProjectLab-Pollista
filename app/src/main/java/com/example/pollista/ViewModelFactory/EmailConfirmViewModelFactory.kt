package com.example.pollista.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pollista.ViewModel.AccountViewModel
import com.example.pollista.ViewModel.EmailConfirmViewModel

class EmailConfirmViewModelFactory (private val userId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmailConfirmViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EmailConfirmViewModel(userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}