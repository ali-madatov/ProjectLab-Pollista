package com.example.pollista.ViewModel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.pollista.DataAccess.Repository.HomePostsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = HomePostsRepository()
    private val refreshTrigger = MutableStateFlow(Unit)
    val posts = refreshTrigger.flatMapLatest {
        repository.getHomePostsDetails().flow.cachedIn(viewModelScope)
    }.asLiveData()


    private val _voteActionState = MutableLiveData<VoteActionState>()
    val voteActionState: LiveData<VoteActionState> get() = _voteActionState

    sealed class VoteActionState {
        object Success : VoteActionState()
        data class Failure(val exception: Exception) : VoteActionState()
    }

    fun onVoteAction(postID: String, isUpVote: Boolean){
        viewModelScope.launch {
            try {
                repository.onVoteAction(postID, isUpVote)
                _voteActionState.value = VoteActionState.Success
                refreshTrigger.emit(Unit)
            } catch (e: Exception){
                _voteActionState.value = VoteActionState.Failure(e)
            }

        }
    }
}