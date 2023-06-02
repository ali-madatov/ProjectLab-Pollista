package com.example.pollista.DataAccess.Model

import android.net.Uri
import com.google.firebase.Timestamp

data class PostModel(
    var postID: String = "",
    var userID: String = "",
    var image1: String = "",
    var image2: String = "",
    var description: String = "",
    var tags: List<String> = listOf(),
    var optionOneVoters: List<String> = listOf(),
    var optionTwoVoters: List<String> = listOf(),
    var timestamp: Timestamp = Timestamp.now()
) {
    // No-arg constructor for Firebase
    constructor() : this("", "", "", "", "")
}
