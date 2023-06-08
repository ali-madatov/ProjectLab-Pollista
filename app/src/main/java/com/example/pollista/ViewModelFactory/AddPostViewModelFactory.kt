package com.example.pollista.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pollista.DataAccess.Repository.HomePostsRepository
import com.example.pollista.DataAccess.Repository.UserModelRepository
import com.example.pollista.ViewModel.AddPostViewModel

class AddPostViewModelFactory(private val postsRepository: HomePostsRepository, private val userModelRepository: UserModelRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddPostViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddPostViewModel(postsRepository, userModelRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}