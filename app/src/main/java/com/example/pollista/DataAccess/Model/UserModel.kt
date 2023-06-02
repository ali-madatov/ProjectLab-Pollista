package com.example.pollista.DataAccess.Model

data class UserModel(
    var userID: String,
    var name: String,
    var username: String,
    var password: String,
    var email: String,
    var bio: String,
    var phoneNumber: String,
    var photoUrl: String?,
    var postsNumber: Int,
    var followers: List<String> = listOf(),
    var followings: List<String> = listOf()
) {
    // No-arg constructor for Firebase
    constructor() : this("", "", "", "", "", "", "","",0)
}