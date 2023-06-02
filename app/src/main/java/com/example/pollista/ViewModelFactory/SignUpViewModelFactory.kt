package com.example.pollista.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pollista.ViewModel.AccountViewModel
import com.example.pollista.ViewModel.HomeViewModel
import com.example.pollista.ViewModel.SignUpViewModel

class SignUpViewModelFactory(private val userId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignUpViewModel(userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}