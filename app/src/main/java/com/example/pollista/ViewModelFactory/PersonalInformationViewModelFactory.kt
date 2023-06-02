package com.example.pollista.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pollista.ViewModel.PersonalInformationViewModel
import com.example.pollista.ViewModel.ProfileViewModel
import com.example.pollista.ViewModel.SearchViewModel

class PersonalInformationViewModelFactory(): ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonalInformationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PersonalInformationViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}