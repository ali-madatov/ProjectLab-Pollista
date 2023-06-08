package com.example.pollista.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.pollista.DataAccess.Model.UserModel
import com.example.pollista.DataAccess.Repository.ProfilePostsRepository
import com.example.pollista.DataAccess.Repository.UserModelRepository

class ProfileViewModel : ViewModel() {
    private val postRepository = ProfilePostsRepository()
    private val userDataRepository = UserModelRepository()
    val ownPosts = postRepository.getOwnPosts().flow.cachedIn(viewModelScope).asLiveData()
    val user: LiveData<UserModel> get() = userDataRepository.user

    fun getUser() {
        userDataRepository.fetchUser()
    }
}