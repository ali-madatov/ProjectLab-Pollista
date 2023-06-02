package com.example.pollista.BusinessModel

data class PostDescription(
    val caption: String,
    val tags: List<String> = listOf()
)
