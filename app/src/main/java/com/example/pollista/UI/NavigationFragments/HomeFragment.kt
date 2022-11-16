package com.example.pollista.UI.NavigationFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.pollista.Adapters.PostsAdapter
import com.example.pollista.Model.CachingPostModelRepository
import com.example.pollista.Model.PostModel
import com.example.pollista.ViewModel.AddPostViewModel
import com.example.pollista.ViewModel.AddPostViewModelFactory
import com.example.projectlab_pollista.R
import com.google.firebase.auth.FirebaseAuth
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewModel: AddPostViewModel
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
        val postRepository = CachingPostModelRepository(FirebaseAuth.getInstance().currentUser!!.uid)
        val factory = AddPostViewModelFactory(postRepository)

        viewModel = ViewModelProvider(this,factory).get(AddPostViewModel::class.java)
        val recyclerView = v.findViewById<RecyclerView>(R.id.rvPostsRecyclerView)
        val snapHelper = PagerSnapHelper()
        recyclerView.layoutManager = LinearLayoutManager(requireActivity().applicationContext, LinearLayoutManager.VERTICAL,false)
        postsAdapter = PostsAdapter(viewModel)
        recyclerView.adapter = postsAdapter

        for (range in 0..25){
            //TODO implement the logic considering postID added into PostModel
//            dataList.add(PostModel(1234567,R.drawable.image1,R.drawable.image2,"Help me to make the right choice :)",
//                Arrays.asList("#apple","#samsung","#12pro","#s21ultra")))
        }


        snapHelper.attachToRecyclerView(recyclerView)
        return v
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
    }
}