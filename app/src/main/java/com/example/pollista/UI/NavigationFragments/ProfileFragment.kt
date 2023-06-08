package com.example.pollista.UI.NavigationFragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.pollista.Adapters.GridViewAdapter
import com.example.pollista.DataAccess.Model.PostModel
import com.example.pollista.Modules.GlideApp
import com.example.projectlab_pollista.R
import com.example.pollista.UI.NavigationFragments.BottomSheets.OwnProfileModalBottomSheet
import com.example.pollista.UI.SignInActivity
import com.example.pollista.ViewModel.ProfileViewModel
import com.example.pollista.ViewModelFactory.ProfileViewModelFactory
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var gridViewAdapter: GridViewAdapter
    private lateinit var viewModel: ProfileViewModel

    private lateinit var profilePhotoImg: ShapeableImageView
    private lateinit var tvUsername: TextView
    private lateinit var tvName: TextView
    private lateinit var tvBio: TextView
    private lateinit var tvPostsNumber: TextView
    private lateinit var tvFollowersNumber: TextView
    private lateinit var tvFollowingNumber: TextView
    private var progressBar: ProgressBar? = null

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
        val v = inflater.inflate(R.layout.fragment_profile, container, false)

        val factory = ProfileViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)

        profilePhotoImg = v.findViewById(R.id.imgProfilePhoto)
        tvUsername = v.findViewById(R.id.tvUsername)
        tvName = v.findViewById(R.id.tvName)
        tvBio = v.findViewById(R.id.tvBio)
        tvPostsNumber = v.findViewById(R.id.tvPostsNumber)
        tvFollowersNumber = v.findViewById(R.id.tvFollowersNumber)
        tvFollowingNumber = v.findViewById(R.id.tvFollowingNumber)
        progressBar = v.findViewById(R.id.progressBar)

        val buttonHamburger = v.findViewById<AppCompatButton>(R.id.btnHamburger)
        buttonHamburger.setOnClickListener{
            val ownProfileModalBottomSheet = OwnProfileModalBottomSheet()
            ownProfileModalBottomSheet.show(parentFragmentManager,"OwnProfileModalBottomSheet")
        }
        val recyclerView = v.findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = GridLayoutManager(requireActivity().applicationContext,3)
        gridViewAdapter = GridViewAdapter()
        recyclerView.adapter = gridViewAdapter

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar!!.visibility = View.VISIBLE
        viewModel.getUser()
        subscribeToLiveUserData()
        // Observe the posts
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.ownPosts.asFlow().collect { pagingData: PagingData<PostModel> ->
                viewLifecycleOwner.lifecycleScope.launch {
                    gridViewAdapter.submitData(pagingData)
                }
            }
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun subscribeToLiveUserData(){
        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            // updating UI with the new user data
            tvUsername.text = user.username
            tvName.text = user.name
            tvBio.text = user.bio
            tvPostsNumber.text = user.postsNumber.toString()
            tvFollowersNumber.text = user.followers.size.toString()
            tvFollowingNumber.text = user.followings.size.toString()

            GlideApp.with(this)
                .asBitmap()
                .load(user.photoUrl)
                .apply(
                    RequestOptions
                        .circleCropTransform().placeholder(R.drawable.photo2).error(R.drawable.photo2))
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        profilePhotoImg.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        profilePhotoImg.setImageDrawable(placeholder)
                    }
                })
            progressBar!!.visibility = View.GONE
        })
    }
    private fun backToSignInPage(){
        val intent = Intent(requireActivity(),SignInActivity::class.java)
        startActivity(intent)
    }
}