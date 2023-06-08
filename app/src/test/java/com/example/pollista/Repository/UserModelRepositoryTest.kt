import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.pollista.DataAccess.Model.UserModel
import com.example.pollista.DataAccess.Repository.UserModelRepository
import com.example.pollista.DataAccess.Util.StorageUtilInterface
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.*
import com.google.firebase.storage.StorageReference
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import io.mockk.Runs

@ExperimentalCoroutinesApi
class UserModelRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    private lateinit var userDocument: DocumentReference

    private lateinit var storageReference: StorageReference

    private lateinit var firebaseUtilInterface: StorageUtilInterface

    private lateinit var userID: String
    private lateinit var userModelRepository: UserModelRepository
    private val userModel = UserModel()
    private val IMAGE_URL = "imageUrl"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        userDocument = mockk(relaxed = true)
        storageReference = mockk(relaxed = true)
        firebaseUtilInterface = mockk(relaxed = true)

        userID = "testUserId"
        userModelRepository = UserModelRepository(userDocument,
            storageReference, userID, firebaseUtilInterface,
            testDispatcher)

        coEvery { firebaseUtilInterface.uploadImageToFirebase(any(), any(), any()) } returns IMAGE_URL
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
        clearAllMocks()
    }

    @Test(expected = RuntimeException::class)
    fun `addUserModel failure`() = testScope.runBlockingTest {
        val exception = RuntimeException("User model addition failed")

        coEvery { userDocument.set(userModel) } throws exception

        val observer = mockk<Observer<UserModel>>(relaxed = true)
        userModelRepository.user.observeForever(observer)

        userModelRepository.addUserModel(userModel)

        advanceUntilIdle()

        coVerify(exactly = 0) { observer.onChanged(any()) }
        confirmVerified(observer)
    }

    @Test
    fun `fetchUser success`() = testScope.runBlockingTest {
        val documentSnapshot: DocumentSnapshot = mockk(relaxed = true)
        val exception: FirebaseFirestoreException? = null

        every { documentSnapshot.toObject(UserModel::class.java) } returns userModel
        every { userDocument.addSnapshotListener(any()) } answers {
            val valueEventListener = firstArg<EventListener<DocumentSnapshot>>()
            valueEventListener.onEvent(documentSnapshot, exception)
            mockk<ListenerRegistration>()
        }

        val observer = mockk<Observer<UserModel>>(relaxed = true)
        userModelRepository.user.observeForever(observer)

        userModelRepository.fetchUser()

        advanceUntilIdle()

    }


    @Test
    fun `updateUserModel with photoUrl success`() = testScope.runBlockingTest {
        val username = "testUsername"
        val email = "testEmail"
        val name = "testName"
        val bio = "testBio"
        val phone = "testPhone"
        val profilePhotoUrl = IMAGE_URL

        val expectedMap = hashMapOf<String, Any>(
            "username" to username,
            "email" to email,
            "name" to name,
            "bio" to bio,
            "phoneNumber" to phone,
            "photoUrl" to profilePhotoUrl
        )

        coEvery { userDocument.update(expectedMap) } returns Tasks.forResult(null)

        userModelRepository.updateUserModel(username, email, name, bio, phone, profilePhotoUrl)

        advanceUntilIdle()

        coVerify(exactly = 1) { firebaseUtilInterface.uploadImageToFirebase(any(), any(), any()) }
        coVerify(exactly = 1) { userDocument.update(expectedMap) }
    }


    @Test
    fun `updateUserModel without image success`() = testScope.runBlockingTest {
        val expectedMap = mapOf(
            "username" to "testUsername",
            "email" to "testEmail",
            "name" to "testName",
            "bio" to "testBio",
            "phoneNumber" to "testPhone"
        )

        coEvery { userDocument.update(expectedMap) } returns Tasks.forResult(null)

        userModelRepository.updateUserModel("testUsername", "testEmail", "testName", "testBio", "testPhone")

        coVerify { userDocument.update(expectedMap) }
    }

    @Test
    fun `notifyPostAddedChange success`() = testScope.runBlockingTest {
        coEvery { userDocument.update("postsNumber", FieldValue.increment(1)) } returns Tasks.forResult(null)

        userModelRepository.notifyPostAddedChange()

        coVerify { userDocument.update("postsNumber", any()) }
    }


}
