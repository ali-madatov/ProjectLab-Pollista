import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.pollista.BusinessModel.AddPostDetails
import com.example.pollista.DataAccess.Repository.HomePostsRepository
import com.example.pollista.DataAccess.Repository.UserModelRepository
import com.example.pollista.ViewModel.AddPostViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class AddPostViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var postRepository: HomePostsRepository

    @Mock
    private lateinit var userDataRepository: UserModelRepository

    private lateinit var viewModel: AddPostViewModel
    private val firstImageUri = mock(Uri::class.java)
    private val secondImageUri = mock(Uri::class.java)
    private val rawDescription = "Sample description"
    private val addPostDetails = AddPostDetails(firstImageUri, secondImageUri, "CAPTION", listOf())

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        MockitoAnnotations.openMocks(this)
        viewModel = AddPostViewModel(postRepository, userDataRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `sharePost success`() = runBlockingTest {
        // Mock the postRepository's addNewPost method
        Mockito.`when`(postRepository.addNewPost(addPostDetails)).then {  }

        // Mock the userDataRepository's notifyPostAddedChange method
        Mockito.`when`(userDataRepository.notifyPostAddedChange()).then { }

        // Create an observer for the postUploadState LiveData
        val observer = mock(Observer::class.java) as Observer<AddPostViewModel.PostUploadState>
        viewModel.postUploadState.observeForever(observer)

        // Call the method to be tested
        viewModel.sharePost(firstImageUri, secondImageUri, rawDescription)

        // Verify that the expected state is emitted
        Mockito.verify(observer).onChanged(AddPostViewModel.PostUploadState.Success)
    }

    @Test
    fun `sharePost failure`() = runBlockingTest {
        val exception = RuntimeException("Post upload failed")

        // Mock the postRepository's addNewPost method to throw an exception
        Mockito.doThrow(exception).`when`(postRepository).addNewPost(addPostDetails)
        // Mock the userDataRepository's notifyPostAddedChange method
        Mockito.doThrow(exception).`when`(userDataRepository).notifyPostAddedChange()

        // Create an observer for the postUploadState LiveData
        val observer = mock(Observer::class.java) as Observer<AddPostViewModel.PostUploadState>
        viewModel.postUploadState.observeForever(observer)

        // Call the method to be tested
        viewModel.sharePost(firstImageUri, secondImageUri, rawDescription)

        // Verify that the expected failure state is emitted
        Mockito.verify(observer).onChanged(AddPostViewModel.PostUploadState.Failure(exception))
    }
}
