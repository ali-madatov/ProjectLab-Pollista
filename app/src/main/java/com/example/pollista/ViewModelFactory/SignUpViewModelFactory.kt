package com.example.pollista.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pollista.DataAccess.Repository.UserModelRepository
import com.example.pollista.ViewModel.AccountViewModel
import com.example.pollista.ViewModel.HomeViewModel
import com.example.pollista.ViewModel.SignUpViewModel

class SignUpViewModelFactory(private val userModelRepository: UserModelRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignUpViewModel(userModelRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}