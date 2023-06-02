package com.example.pollista.BusinessModel

import android.net.Uri
import com.google.firebase.Timestamp

data class HomePostDetails(
    var postID: String,
    var firstImageUri: Uri,
    var secondImageUri: Uri,
    var userCoreDetails: UserCoreDetails,
    var postDescription: PostDescription,
    var voterDetails: VoterDetails,
    var timestamp: Timestamp
)
