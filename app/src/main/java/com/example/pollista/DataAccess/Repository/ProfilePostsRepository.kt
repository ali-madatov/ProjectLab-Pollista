package com.example.pollista.DataAccess.Repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.pollista.DataAccess.Model.PostModel
import com.example.pollista.DataAccess.PagingSource.UserBasedFilteringPostPagingSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfilePostsRepository() {
    // Initialize the Firestore and Firebase Storage instances
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val firestore = Firebase.firestore
    private val maxPostUploadSize: Long = 6

    fun getOwnPosts(): Pager<DocumentSnapshot, PostModel>{
        return Pager(
            config = PagingConfig(
                pageSize = maxPostUploadSize.toInt(),
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UserBasedFilteringPostPagingSource(firestore, currentUser!!.uid, maxPostUploadSize) }
        )
    }
}