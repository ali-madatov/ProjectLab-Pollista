package com.example.pollista.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pollista.DataAccess.Model.UserModel
import com.example.pollista.DataAccess.Repository.UserModelRepository
import kotlinx.coroutines.launch

class SignUpViewModel(private val userId: String) : ViewModel() {
    val repository = UserModelRepository()

    private val _userAddState = MutableLiveData<UserAddState>()
    val userAddState: LiveData<UserAddState> get() = _userAddState

    sealed class UserAddState {
        object Success : UserAddState()
        data class Failure(val exception: Exception) : UserAddState()
    }

    fun saveUserData(uid: String, email: String?, username: String, password: String){
        val userModel = UserModel(uid, "", username, password, email.toString(), "","", null, 0)

        viewModelScope.launch {
            try {
                repository.addUserModel(userModel)
                _userAddState.value = UserAddState.Success
            } catch (e: Exception) {
                _userAddState.value = UserAddState.Failure(e)
            }
        }
    }

    fun saveUserData(uid: String, email: String?){
        saveUserData(uid, email, "", "")
    }
}