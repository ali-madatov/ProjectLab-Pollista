package com.example.pollista.DataAccess.PagingSource
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pollista.DataAccess.Model.PostModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ExplorePostPagingSource(
    private val firestore: FirebaseFirestore,
    private val maxUploadSize: Long
) : PagingSource<DocumentSnapshot, PostModel>() {

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, PostModel> = withContext(
        Dispatchers.IO) {
        return@withContext try {
            val currentPage = params.key?.let {
                firestore.collection("posts")
                    .startAfter(it)
                    .limit(maxUploadSize)
                    .get()
                    .await()
            } ?: firestore.collection("posts")
                .limit(maxUploadSize)
                .get()
                .await()

            val posts = currentPage.toObjects(PostModel::class.java)
            val lastDocumentSnapshot = currentPage.documents.lastOrNull()

            LoadResult.Page(
                data = posts,
                prevKey = null,
                nextKey = lastDocumentSnapshot
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<DocumentSnapshot, PostModel>): DocumentSnapshot? {
        return state.pages.last().nextKey
    }
}