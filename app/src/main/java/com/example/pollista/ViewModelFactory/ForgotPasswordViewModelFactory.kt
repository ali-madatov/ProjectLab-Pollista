package com.example.pollista.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pollista.ViewModel.AccountViewModel
import com.example.pollista.ViewModel.ForgotPasswordViewModel
import com.example.pollista.ViewModel.HomeViewModel

class ForgotPasswordViewModelFactory(private val userId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ForgotPasswordViewModel(userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}