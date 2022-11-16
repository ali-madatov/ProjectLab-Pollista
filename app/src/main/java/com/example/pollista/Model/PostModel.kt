package com.example.pollista.Model

import android.net.Uri

data class PostModel(
    val postID: String,
    var userID: String,
    var image1: Uri,
    var image2: Uri,
    var description: String,
    var tags: List<String>
)
