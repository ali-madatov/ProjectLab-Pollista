package com.example.pollista.DataAccess.Repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.pollista.BusinessModel.AddPostDetails
import com.example.pollista.BusinessModel.HomePostDetails
import com.example.pollista.DataAccess.Model.PostModel
import com.example.pollista.DataAccess.PagingSource.HomePostPagingSource
import com.example.pollista.DataAccess.Util.FirebaseStorageUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

class HomePostsRepository {

    // Initialize the Firestore and Firebase Storage instances
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val firestore = Firebase.firestore
    private val firebaseStorage = Firebase.storage
    private val maxPostUploadSize: Long = 1

    //constants
    private companion object{
        private const val TAG = "HOME_POSTS_REPOSITORY"
        private const val IMAGE1_NAME = "image1"
        private const val IMAGE2_NAME = "image2"

    }

    suspend fun addNewPost(postDetails: AddPostDetails) = withContext(Dispatchers.IO)  {
        val postID = generatePostID()
        // Upload the images to Firebase Storage
        val firstImageUrl = FirebaseStorageUtil.uploadImageToFirebase(
            firebaseStorage.reference.child("images/${postID}/${IMAGE1_NAME}"),
            postDetails.firstImageUri,
            onException = { exception ->
                val message = "Failed to upload ${postDetails.firstImageUri} to Firebase Storage"
                Log.d(TAG, message, exception)
            }
        )
        val secondImageUrl = FirebaseStorageUtil.uploadImageToFirebase(
            firebaseStorage.reference.child("images/${postID}/${IMAGE2_NAME}"),
            postDetails.secondImageUri,
            onException = { exception ->
                val message = "Failed to upload ${postDetails.secondImageUri} to Firebase Storage"
                Log.d(TAG, message, exception)
            }
        )
        firstImageUrl?.let { firstImage ->
            secondImageUrl?.let { secondImage ->
                val postModel = PostModel(
                    postID,
                    currentUser!!.uid,
                    firstImage,
                    secondImage,
                    postDetails.caption,
                    postDetails.tags
                )
                val postDocRef = firestore.collection("posts").document(postModel.postID)
                try {
                    postDocRef.set(postModel).await()
                    Log.d(TAG, "Post successfully added")
                } catch (e: Exception) {
                    Log.d(TAG, "Failed to save post!", e)
                    throw e // Rethrowing the exception to propagate it to the scope
                }
            }
        }
    }

    suspend fun onVoteAction(postID: String, isFirstOption: Boolean) = withContext(Dispatchers.IO){
        val postDocRef = firestore.collection("posts").document(postID)
        val fieldName: String = isFirstOption.let {
            if (isFirstOption) "optionOneVoters"
            else "optionTwoVoters"
        }
        Log.d(TAG, "Saving vote action")
        postDocRef.update(fieldName, FieldValue.arrayUnion(currentUser!!.uid))
            .await()
    }

    fun getHomePostsDetails(): Pager<DocumentSnapshot, HomePostDetails> {
        return Pager(
            config = PagingConfig(
                pageSize = maxPostUploadSize.toInt(),
                enablePlaceholders = false
            ),
            pagingSourceFactory = { HomePostPagingSource(currentUser!!.uid, firestore, maxPostUploadSize) }
        )
    }

    private fun generatePostID(): String {
        return UUID.randomUUID().toString()
    }
}


