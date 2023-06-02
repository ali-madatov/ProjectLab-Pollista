package com.example.pollista.DataAccess.Model

import com.google.firebase.Timestamp


data class VoteModel(
    val voteID: String,
    val postID: String,
    val userID: String,
    val isUpVote: Boolean,
    val timestamp: Timestamp = Timestamp.now()
)
