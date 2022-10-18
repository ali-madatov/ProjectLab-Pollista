package com.example.pollista.Model

data class PostModel(
    var userID: Int,
    var image1: Int,
    var image2: Int,
    var description: String,
    var tags: List<String>
)
