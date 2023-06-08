package com.example.pollista.ViewModel

import android.net.Uri
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.pollista.DataAccess.Model.PostModel
import com.example.pollista.DataAccess.Model.UserModel
import com.example.pollista.DataAccess.Repository.ProfilePostsRepository
import com.example.pollista.DataAccess.Repository.UserModelRepository
import com.example.pollista.DataAccess.Util.FirebaseStorageUtil
import kotlinx.coroutines.launch
import java.util.*

class PersonalInformationViewModel : ViewModel() {
    private val userDataRepository = UserModelRepository()
    val user: LiveData<UserModel> get() = userDataRepository.user

    private val _dataUpdateState = MutableLiveData<DataUpdateState>()
    val dataUpdateState: LiveData<DataUpdateState> get() = _dataUpdateState

    sealed class DataUpdateState {
        object Success : DataUpdateState()
        data class Failure(val exception: Exception) : DataUpdateState()
    }

    fun getUser() {
        userDataRepository.fetchUser()
    }

    fun updateUserData(username: String, email: String, name: String, bio: String, phone: String, profilePhotoUrl: String) {
        viewModelScope.launch {
            try {
                userDataRepository.updateUserModel(username, email, name, bio, phone, profilePhotoUrl)
                _dataUpdateState.value = DataUpdateState.Success
            } catch (e: Exception) {
                _dataUpdateState.value = DataUpdateState.Failure(e)
            }
        }
    }

    fun updateUserData(username: String, email: String, name: String, bio: String, phone: String) {
        viewModelScope.launch {
            try {
                userDataRepository.updateUserModel(username, email, name, bio, phone)
                _dataUpdateState.value = DataUpdateState.Success
            } catch (e: Exception) {
                _dataUpdateState.value = DataUpdateState.Failure(e)
            }
        }
    }

}