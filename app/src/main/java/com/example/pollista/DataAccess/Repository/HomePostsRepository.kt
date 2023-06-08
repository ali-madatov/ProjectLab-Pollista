package com.example.pollista.DataAccess.Repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.pollista.BusinessModel.AddPostDetails
import com.example.pollista.BusinessModel.HomePostDetails
import com.example.pollista.DataAccess.Model.PostModel
import com.example.pollista.DataAccess.PagingSource.HomePostPagingSource
import com.example.pollista.DataAccess.Util.FirebaseStorageUtil
import com.example.pollista.DataAccess.Util.StorageUtilInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

open class HomePostsRepository (private val postCollection: CollectionReference,
                                private val storageReference: StorageReference,
                                private val userID: String,
                                private val utilInterface: StorageUtilInterface,
                                private val coroutineDispatcher: CoroutineDispatcher
) {

    // Initialize the Firestore and Firebase Storage instances
    private val maxPostUploadSize: Long = 1

    constructor(): this(Firebase.firestore.collection("posts"),
        Firebase.storage.reference,
        FirebaseAuth.getInstance().currentUser!!.uid,
        FirebaseStorageUtil,
        Dispatchers.IO)

    //constants
    private companion object{
        private const val TAG = "HOME_POSTS_REPOSITORY"
        private const val IMAGE1_NAME = "image1"
        private const val IMAGE2_NAME = "image2"

    }

    open suspend fun addNewPost(postDetails: AddPostDetails) = withContext(coroutineDispatcher)  {
        val postID = generatePostID()
        // Upload the images to Firebase Storage
        val firstImageUrl = utilInterface.uploadImageToFirebase(
            storageReference.child("images/${postID}/${IMAGE1_NAME}"),
            postDetails.firstImageUri,
            onException = { exception ->
                val message = "Failed to upload ${postDetails.firstImageUri} to Firebase Storage"
                Log.d(TAG, message, exception)
            }
        )
        val secondImageUrl = utilInterface.uploadImageToFirebase(
            storageReference.child("images/${postID}/${IMAGE2_NAME}"),
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
                    userID,
                    firstImage,
                    secondImage,
                    postDetails.caption,
                    postDetails.tags
                )
                val postDocRef = postCollection.document(postModel.postID)
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

    open suspend fun onVoteAction(postID: String, isFirstOption: Boolean) = withContext(coroutineDispatcher){
        val postDocRef = postCollection.document(postID)
        val fieldName: String = isFirstOption.let {
            if (isFirstOption) "optionOneVoters"
            else "optionTwoVoters"
        }
        Log.d(TAG, "Saving vote action")
        postDocRef.update(fieldName, FieldValue.arrayUnion(userID))
            .await()
    }

    open fun getHomePostsDetails(): Pager<DocumentSnapshot, HomePostDetails> {
        return Pager(
            config = PagingConfig(
                pageSize = maxPostUploadSize.toInt(),
                enablePlaceholders = false
            ),
            pagingSourceFactory = { HomePostPagingSource(userID, postCollection.firestore, maxPostUploadSize) }
        )
    }

    private fun generatePostID(): String {
        return UUID.randomUUID().toString()
    }
}


