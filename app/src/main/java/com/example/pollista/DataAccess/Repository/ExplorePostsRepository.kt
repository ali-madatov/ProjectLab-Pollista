package com.example.pollista.DataAccess.Repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.pollista.DataAccess.Model.PostModel
import com.example.pollista.DataAccess.PagingSource.ExplorePostPagingSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ExplorePostsRepository() {
    // Initialize the Firestore and Firebase Storage instances
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val firestore = Firebase.firestore
    private val maxPostUploadSize: Long = 8

    fun getRecommendedPosts(): Pager<DocumentSnapshot, PostModel>{
        return Pager(
            config = PagingConfig(
                pageSize = 1,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ExplorePostPagingSource(firestore, maxPostUploadSize) }
        )
    }
}