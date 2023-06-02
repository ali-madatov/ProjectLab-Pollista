package com.example.pollista.BusinessModel

import android.net.Uri

data class AddPostDetails(
    val firstImageUri: Uri,
    val secondImageUri: Uri,
    val caption: String,
    val tags: List<String> = listOf()
)
