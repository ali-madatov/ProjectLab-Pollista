package com.example.pollista.DataAccess.Mapper

import android.net.Uri
import com.example.pollista.BusinessModel.HomePostDetails
import com.example.pollista.BusinessModel.PostDescription
import com.example.pollista.BusinessModel.UserCoreDetails
import com.example.pollista.DataAccess.Model.PostModel
import com.example.pollista.DataAccess.Model.UserModel
import com.example.pollista.DataAccess.Util.VoteDetectionUtil

class HomePostDetailsMapper(val requesterUserID: String) {

    companion object{
        private const val UNKNOWN_USER_NAME = "unknown_user"
    }

    fun mapFrom(postModel: PostModel, userModel: UserModel?): HomePostDetails{
        val userCoreDetails = getUserOrUnknown(userModel)
        val voterDetails = VoteDetectionUtil.getVoterDetails(
            requesterUserID,
            postModel.optionOneVoters,
            postModel.optionTwoVoters
        )
        // Return a new mapped object
        return HomePostDetails(
            postModel.postID,
            Uri.parse(postModel.image1),
            Uri.parse(postModel.image2),
            userCoreDetails,
            PostDescription(postModel.description, postModel.tags),
            voterDetails,
            postModel.timestamp
        )
    }

    private fun getUserOrUnknown(userModel: UserModel?): UserCoreDetails {
        var userID: String? = null
        var username = UNKNOWN_USER_NAME
        var photoUrl: Uri? = null
        userModel?.let { model ->
            userID = userModel.userID
            username = model.username
            model.photoUrl?.let { photoUrl = Uri.parse(it) }
        }
        return UserCoreDetails(userID, username, photoUrl)
    }
}