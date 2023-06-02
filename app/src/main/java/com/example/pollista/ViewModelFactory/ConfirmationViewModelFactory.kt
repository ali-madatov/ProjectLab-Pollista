package com.example.pollista.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pollista.ViewModel.AccountViewModel
import com.example.pollista.ViewModel.ConfirmationViewModel

class ConfirmationViewModelFactory (private val userId: String?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConfirmationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConfirmationViewModel(userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}