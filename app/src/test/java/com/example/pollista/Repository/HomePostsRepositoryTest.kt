package com.example.pollista.Repository

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.pollista.BusinessModel.AddPostDetails
import com.example.pollista.DataAccess.Repository.HomePostsRepository
import com.example.pollista.DataAccess.Util.StorageUtilInterface
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.StorageReference
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class HomePostsRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    private lateinit var postCollection: CollectionReference
    private lateinit var storageReference: StorageReference
    private lateinit var utilInterface: StorageUtilInterface
    private lateinit var userID: String
    private lateinit var homePostsRepository: HomePostsRepository
    private val firstImageUri = Mockito.mock(Uri::class.java)
    private val secondImageUri = Mockito.mock(Uri::class.java)
    private val addPostDetails = AddPostDetails(firstImageUri, secondImageUri,
    "caption", listOf())

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        postCollection = mockk(relaxed = true)
        storageReference = mockk(relaxed = true)
        utilInterface = mockk(relaxed = true)

        userID = "testUserId"
        homePostsRepository = HomePostsRepository(postCollection, storageReference, userID, utilInterface, testDispatcher)

        coEvery { utilInterface.uploadImageToFirebase(any(), any(), any()) } returns "imageUrl"
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
        clearAllMocks()
    }

    @Test
    fun `addNewPost success`() = testScope.runBlockingTest {
        val postDocument = mockk<DocumentReference>(relaxed = true)
        every { postCollection.document(any()) } returns postDocument
        every { postDocument.set(any()) } returns Tasks.forResult(null)

        homePostsRepository.addNewPost(addPostDetails)

        advanceUntilIdle()

        verify(exactly = 1) { postDocument.set(any()) }
    }

    @Test
    fun `onVoteAction success`() = testScope.runBlockingTest {
        val postID = "testPostID"
        val isFirstOption = true

        val postDocument = mockk<DocumentReference>(relaxed = true)
        every { postCollection.document(any()) } returns postDocument
        every { postDocument.update(any<String>(), any()) } returns Tasks.forResult(null)

        homePostsRepository.onVoteAction(postID, isFirstOption)

        advanceUntilIdle()

        verify(exactly = 1) { postDocument.update(any<String>(), any()) }
    }
}
