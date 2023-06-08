package com.example.pollista.ViewModel

import androidx.lifecycle.*
import com.example.pollista.DataAccess.Repository.UserModelRepository

class AccountViewModel : ViewModel() {
    private val userDataRepository = UserModelRepository()
    val profilePhotoUrl: LiveData<String?> get() = userDataRepository.user.map {
        it.photoUrl
    }

    fun getProfilePhotoUrl(){
        userDataRepository.fetchUser()
    }
}