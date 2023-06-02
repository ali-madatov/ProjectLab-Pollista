package com.example.pollista.ViewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pollista.BusinessModel.AddPostDetails
import com.example.pollista.DataAccess.Repository.HomePostsRepository
import com.example.pollista.DataAccess.Repository.UserModelRepository
import kotlinx.coroutines.launch

class AddPostViewModel(private val userId: String): ViewModel() {

    private val postRepository = HomePostsRepository()
    private val userDataRepository = UserModelRepository()

    private val _postUploadState = MutableLiveData<PostUploadState>()
    val postUploadState: LiveData<PostUploadState> get() = _postUploadState

    sealed class PostUploadState {
        object Success : PostUploadState()
        data class Failure(val exception: Exception) : PostUploadState()
    }

    fun getRepository(): HomePostsRepository {
        return postRepository
    }

    fun sharePost(firstImageUri: Uri, secondImageUri: Uri, rawDescription: String) {
        val description = processDescription(rawDescription)
        val postDetails = AddPostDetails(
            firstImageUri,
            secondImageUri,
            description.caption,
            description.tags
        )

        viewModelScope.launch {
            try {
                postRepository.addNewPost(postDetails)
                userDataRepository.notifyPostAddedChange()
                _postUploadState.value = PostUploadState.Success
            } catch (e: Exception) {
                _postUploadState.value = PostUploadState.Failure(e)
            }
        }
    }

    private fun processDescription(rawDescription: String): Description{
        //currently returns the whole raw description as a caption
        return Description(rawDescription)
    }

    data class Description(
        val caption: String,
        val tags: List<String> = listOf()
    )
}

