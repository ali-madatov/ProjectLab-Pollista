package com.example.pollista.UI.NavigationFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.pollista.Adapters.PostsAdapter
import com.example.pollista.BusinessModel.HomePostDetails
import com.example.pollista.DataAccess.Model.PostModel
import com.example.pollista.ViewModel.AddPostViewModel
import com.example.pollista.ViewModel.HomeViewModel
import com.example.pollista.ViewModelFactory.HomeViewModelFactory
import com.example.projectlab_pollista.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), OnVoteClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewModel: HomeViewModel
    private lateinit var postsAdapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_home, container, false)

        val factory = HomeViewModelFactory(FirebaseAuth.getInstance().currentUser!!.uid)

        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        postsAdapter = PostsAdapter(this)
        val recyclerView = v.findViewById<RecyclerView>(R.id.rvPostsRecyclerView)
        val snapHelper = PagerSnapHelper()
        recyclerView.layoutManager = LinearLayoutManager(requireActivity().applicationContext, LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = postsAdapter

        snapHelper.attachToRecyclerView(recyclerView)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the posts
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.posts.asFlow().collect { pagingData: PagingData<HomePostDetails> ->
                viewLifecycleOwner.lifecycleScope.launch {
                    postsAdapter.submitData(pagingData)
                }
            }
        }

        viewModel.voteActionState.observe(this) { state ->
            when (state) {
                is HomeViewModel.VoteActionState.Success -> {
                    Log.d(TAG, "Vote action saved successfully")
                }
                is HomeViewModel.VoteActionState.Failure -> {
                    Log.w(TAG, "Failed to save vote action!", state.exception)
                    showShortToast("Failed to save the vote action")
                }
            }
        }
    }

    override fun onVoteClick(postId: String, isUpVote: Boolean) {
        viewModel.onVoteAction(postId, isUpVote)
    }

    private fun showShortToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        private const val TAG = "HOME_FRAGMENT"
    }
}