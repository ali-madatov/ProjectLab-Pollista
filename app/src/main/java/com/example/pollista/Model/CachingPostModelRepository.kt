package com.example.pollista.Model

import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.pollista.UI.AddPostActivity
import com.example.pollista.Utils.FirebaseReferenceUtil
import com.example.projectlab_pollista.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.github.reactivecircus.cache4k.Cache
import java.io.File
import java.util.Arrays.asList
import kotlin.time.Duration.Companion.minutes


class CachingPostModelRepository(val userId: String) {
    private var storageReference: StorageReference = FirebaseStorage.getInstance().getReference("users")
    private var userStorageReference: StorageReference = FirebaseStorage.getInstance().getReference("users/".plus(userId))
    private var postsCache = Cache.Builder().expireAfterAccess(10.minutes).build<String, PostModel>()
    private var loadFlag = false

    companion object{
        private const val TAG: String = "Post Model Repository"
        private const val MAX_BYTES: Long = 1024*1024
    }

    fun addPostModel(postModel: PostModel){
        try {
            userStorageReference.child(postModel.postID).child(FirebaseReferenceUtil.FIRST_IMAGE).putFile(postModel.image1)
                .addOnSuccessListener {
                    userStorageReference.child(postModel.postID).child(FirebaseReferenceUtil.SECOND_IMAGE).putFile(postModel.image2)
                        .addOnSuccessListener {
                            Log.d(TAG, "CachingPostModelRepository: Successfully uploaded images!")
                            //saving caption
                            userStorageReference.child(postModel.postID).child(FirebaseReferenceUtil.CAPTION).putBytes(postModel.description.toByteArray())
                                .addOnSuccessListener {
                                    Log.d(TAG, "CachingPostModelRepository: Successfully uploaded caption!")
                                }
                                .addOnFailureListener{
                                    Log.d(TAG, "CachingPostModelRepository: Failed to upload caption!")
                                }
                        }
                        .addOnFailureListener{
                            Log.d(TAG, "CachingPostModelRepository: Failed to upload the second image!")
                        }
                }
                .addOnFailureListener{
                    Log.d(TAG, "CachingPostModelRepository: Failed to upload the first image!")
                }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
        postsCache.put(postModel.postID, postModel)
    }

    fun getAllPostModels(): List<PostModel>{
        loadAllPosts()
        while (!loadFlag){
            Log.d(TAG, "CachingPostModelRepository: waiting for the models to be loaded...")
        }
        return postsCache.asMap().values.toList()
    }

    fun loadAllPosts(){
        var lines: List<String> = emptyList()
        userStorageReference
            .child(FirebaseReferenceUtil.FOLLOWING_LIST)
            .getBytes(MAX_BYTES)
            .addOnSuccessListener{
                try{
                    val value: String = it.decodeToString()
                    lines = value.split(" ")
                    lines.forEach{ followingUserId ->
                        storageReference
                            .child(followingUserId)
                            .child(FirebaseReferenceUtil.POST_LIST)
                            .getBytes(MAX_BYTES)
                            .addOnSuccessListener { second ->
                                try{
                                    val postIDList: List<String>  = second.decodeToString().split(" ")
                                    postIDList.forEach{ postID ->
                                        retrievePostModel(userId, postID)?.let { postModel ->
                                            postsCache.put(postID, postModel)
                                        }
                                    }
                                }
                                catch (e: Exception){
                                    e.printStackTrace()
                                }

                            }
                            .addOnFailureListener{
                                it.printStackTrace()
                                Log.d(TAG, "CachingPostModelRepository: Failed to retrieve posts from user ".plus(followingUserId))
                            }
                    }
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
            }
            .addOnFailureListener{
                Log.d(TAG, "CachingPostModelRepository: Failed to retrieve followings list for user ".plus(userId))
            }
            .addOnCompleteListener{
                loadFlag = true
            }

    }

    private fun retrievePostModel(userID: String, postID: String): PostModel? {
        val postReference = userStorageReference.parent!!.child(userID).child(postID)
        var postModel: PostModel? = null
        postReference.child(FirebaseReferenceUtil.FIRST_IMAGE)
            .downloadUrl
            .addOnSuccessListener { firstImageUri ->
                postReference.child(FirebaseReferenceUtil.SECOND_IMAGE)
                    .downloadUrl
                    .addOnSuccessListener {  secondImageUri ->
                        postReference.child(FirebaseReferenceUtil.CAPTION)
                            .getBytes(MAX_BYTES)
                            .addOnSuccessListener {  byteArray ->
                                val caption: String = byteArray.decodeToString()
                                //TODO implement tag logic
                                postModel = PostModel(postID, userID, firstImageUri, secondImageUri, caption, emptyList())
                            }
                            .addOnFailureListener{
                                it.printStackTrace()
                            }
                    }
                    .addOnFailureListener{
                        it.printStackTrace()
                    }
            }
            .addOnFailureListener{
                it.printStackTrace()
            }
        return postModel
    }


}