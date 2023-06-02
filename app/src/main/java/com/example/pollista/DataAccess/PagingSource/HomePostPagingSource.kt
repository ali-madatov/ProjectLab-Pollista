package com.example.pollista.DataAccess.PagingSource
import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pollista.BusinessModel.HomePostDetails
import com.example.pollista.BusinessModel.PostDescription
import com.example.pollista.BusinessModel.UserCoreDetails
import com.example.pollista.DataAccess.Mapper.HomePostDetailsMapper
import com.example.pollista.DataAccess.Model.PostModel
import com.example.pollista.DataAccess.Model.UserModel
import com.example.pollista.DataAccess.Util.VoteDetectionUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomePostPagingSource(
    private val requesterUserID: String,
    private val firestore: FirebaseFirestore,
    private val maxUploadSize: Long
) : PagingSource<DocumentSnapshot, HomePostDetails>() {

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, HomePostDetails> = withContext(
        Dispatchers.IO) {
        return@withContext try {
            val currentPostPage = params.key?.let {
                firestore.collection("posts")
                    .startAfter(it)
                    .limit(maxUploadSize)
                    .get()
                    .await()
            } ?: firestore.collection("posts")
                .limit(maxUploadSize)
                .get()
                .await()

            val postDocuments = currentPostPage.toObjects(PostModel::class.java)
            val lastDocumentSnapshot = currentPostPage.documents.lastOrNull()

            val userModels = getUserModels(postDocuments.mapNotNull { postModel -> postModel.userID })

            //mapping
            val mapper = HomePostDetailsMapper(requesterUserID)
            val posts = postDocuments.mapNotNull { postModel ->
                mapper.mapFrom(
                    postModel,
                    userModels.firstOrNull { userModel -> userModel.userID.equals(postModel.userID) }
                )
            }

            LoadResult.Page(
                data = posts,
                prevKey = null,
                nextKey = lastDocumentSnapshot
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<DocumentSnapshot, HomePostDetails>): DocumentSnapshot? {
        return state.pages.last().nextKey
    }

    private suspend fun getUserModels(userIDs: List<String>): List<UserModel>{
        val currentUserPage = firestore.collection("users")
            .whereIn("userID", userIDs)
            .get()
            .await()

        return currentUserPage.toObjects(UserModel::class.java)
    }

}
