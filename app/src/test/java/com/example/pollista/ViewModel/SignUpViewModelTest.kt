package com.example.pollista.ViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.pollista.DataAccess.Model.UserModel
import com.example.pollista.DataAccess.Repository.UserModelRepository
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
import org.mockito.kotlin.anyOrNull

@ExperimentalCoroutinesApi
class SignUpViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: UserModelRepository

    private lateinit var viewModel: SignUpViewModel

    private val uid = "TEST_UID"
    private val email = "test@example.com"
    private val username = "testuser"
    private val password = "password"
    private val userModel = UserModel()

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        MockitoAnnotations.openMocks(this)
        viewModel = SignUpViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saveUserData success`() = runBlockingTest {

        // Mock the repository's addUserModel method
        Mockito.`when`(repository.addUserModel(userModel)).then {  }

        // Create an observer for the userAddState LiveData
        val observer = Mockito.mock(Observer::class.java) as Observer<SignUpViewModel.UserAddState>
        viewModel.userAddState.observeForever(observer)

        // Call the method to be tested
        viewModel.saveUserData(uid, email, username, password)

        // Verify that the expected state is emitted
        Mockito.verify(observer).onChanged(SignUpViewModel.UserAddState.Success)
    }

    @Test
    fun `saveUserData failure`() = runBlockingTest {
        val exception = RuntimeException("User data save failed")

        // Mock the repository's addUserModel method to throw an exception
        Mockito.doThrow(exception).`when`(repository).addUserModel(anyOrNull())

        // Create an observer for the userAddState LiveData
        val observer = Mockito.mock(Observer::class.java) as Observer<SignUpViewModel.UserAddState>
        viewModel.userAddState.observeForever(observer)

        // Call the method to be tested
        viewModel.saveUserData(uid, email)

        // Verify that the expected failure state is emitted
        Mockito.verify(observer).onChanged(SignUpViewModel.UserAddState.Failure(exception))
    }
}
