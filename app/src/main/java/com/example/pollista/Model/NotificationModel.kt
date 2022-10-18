package com.example.pollista.Model

data class NotificationModel(
    var userImage: Int,
    var userName: String,
    var action: String,
    var timeElapsed: String,
    var postImage1: Int?,
    var postImage2: Int?
)
