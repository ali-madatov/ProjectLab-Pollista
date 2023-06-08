import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.pollista.DataAccess.Repository.HomePostsRepository
import com.example.pollista.ViewModel.HomeViewModel
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
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: HomePostsRepository

    private lateinit var viewModel: HomeViewModel
    private val postID = "POST_ID"
    private val isUpVote = true

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        MockitoAnnotations.openMocks(this)
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onVoteAction success`() = runBlockingTest {

        Mockito.`when`(repository.onVoteAction(postID, isUpVote)).then {  }

        val observer = Mockito.mock(Observer::class.java) as Observer<HomeViewModel.VoteActionState>
        viewModel.voteActionState.observeForever(observer)

        viewModel.onVoteAction(postID, isUpVote)

        Mockito.verify(observer).onChanged(HomeViewModel.VoteActionState.Success)
    }

    @Test
    fun `onVoteAction failure`() = runBlockingTest {
        val exception = RuntimeException("Vote action failed")

        Mockito.doThrow(exception).`when`(repository).onVoteAction(postID, isUpVote)

        val observer = Mockito.mock(Observer::class.java) as Observer<HomeViewModel.VoteActionState>
        viewModel.voteActionState.observeForever(observer)

        viewModel.onVoteAction(postID, isUpVote)

        Mockito.verify(observer).onChanged(HomeViewModel.VoteActionState.Failure(exception))
    }
}
