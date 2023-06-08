package com.example.pollista.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.pollista.DataAccess.Repository.ExplorePostsRepository

class SearchViewModel : ViewModel() {
    private val repository = ExplorePostsRepository()
    val recommendedPosts = repository.getRecommendedPosts().flow.cachedIn(viewModelScope).asLiveData()
}