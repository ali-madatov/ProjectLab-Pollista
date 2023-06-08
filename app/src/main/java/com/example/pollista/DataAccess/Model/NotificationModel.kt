package com.example.pollista.DataAccess.Model

import com.google.firebase.Timestamp

data class NotificationModel(
    var userImage: Int,
    var userName: String,
    var action: String,
    var timeElapsed: String,
    var postImage1: Int?,
    var postImage2: Int?
)

//data class NotificationModel(
//    var notificationID: String,
//    var userID: String,
//    var userImageUrl: String,
//    var userName: String,
//    var actionType: Int,
//    var postID: String?,
//    var postImage1Url: String?,
//    var postImage2Url: String?,
//    var timestamp: Timestamp = Timestamp.now()
//){
//    constructor(url: Int, userImageUrl: String, userName: String, action: String, postImage1Url: Int?, postImage2Url: Int?):
//            this("","",url.toString(), userName, 1, "", postImage1Url.toString(), postImage2Url.toString())
//}
