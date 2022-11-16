package com.example.pollista.ViewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.pollista.Model.CachingPostModelRepository
import com.example.pollista.Model.PostModel
import java.util.*

class AddPostViewModel(private val repository: CachingPostModelRepository): ViewModel() {
    fun getRepository(): CachingPostModelRepository{
        return repository
    }

    fun sharePost(firstImageUri: Uri, secondImageUri: Uri, caption: String){
        val postId = UUID.randomUUID().toString()
        //TODO implement tag logic
        val postModel = PostModel(postId, repository.userId, firstImageUri, secondImageUri, caption, emptyList())
        repository.addPostModel(postModel)
    }
}