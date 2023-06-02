package com.example.pollista.DataAccess.Repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pollista.BusinessModel.VoteDetails
import com.example.pollista.DataAccess.Model.UserModel
import com.example.pollista.DataAccess.Model.VoteModel
import com.example.pollista.DataAccess.Util.FirebaseStorageUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class VoteModelRepository {
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val firestore = Firebase.firestore
    private val _user = MutableLiveData<UserModel>()
    val user: LiveData<UserModel> get() = _user

    //constants
    private companion object{
        private const val TAG = "VOTE_MODEL_REPOSITORY"
        private const val POST_ID_FIELD_NAME = "postID"
        private const val USER_ID_FIELD_NAME = "userID"
        private const val CHOICE_FIELD_NAME = "isUpVote"
        private const val TIMESTAMP_FIELD_NAME = "timestamp"
    }

    suspend fun addNewVote(voteDetails: VoteDetails) = withContext(Dispatchers.IO) {
        // Get vote doc reference in Firestore
        val voteID = generateVoteID()
        val voteModel = VoteModel(voteID, voteDetails.postID, currentUser!!.uid, voteDetails.isUpVote)
        val docRef = firestore.collection("votes").document(voteID)
        docRef.set(voteModel)
            .addOnSuccessListener {
                Log.d(TAG, "Vote successfully saved")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to save vote: ${voteID}!",it)
            }
    }

    private fun generateVoteID(): String{
        return UUID.randomUUID().toString()
    }
}