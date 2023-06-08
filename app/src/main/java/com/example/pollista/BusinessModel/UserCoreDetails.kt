package com.example.pollista.BusinessModel

import android.net.Uri

data class UserCoreDetails(
    val userID: String?,
    val username: String,
    val photoUrl: Uri?
)
