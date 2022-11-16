package com.example.pollista.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pollista.Model.CachingPostModelRepository

class AddPostViewModelFactory(private val repository: CachingPostModelRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddPostViewModel(repository) as T
    }
}