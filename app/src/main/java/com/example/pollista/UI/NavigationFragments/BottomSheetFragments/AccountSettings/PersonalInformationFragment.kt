package com.example.pollista.UI.NavigationFragments.BottomSheetFragments.AccountSettings

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.pollista.Modules.GlideApp
import com.example.pollista.UI.AddPostActivity
import com.example.pollista.ViewModel.PersonalInformationViewModel
import com.example.pollista.ViewModelFactory.PersonalInformationViewModelFactory
import com.example.projectlab_pollista.R
import com.google.android.material.imageview.ShapeableImageView
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PersonalInformationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonalInformationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewModel: PersonalInformationViewModel
    private var progressBar: ProgressBar? = null
    private lateinit var etUserName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etName: EditText
    private lateinit var etBio: EditText
    private lateinit var etPhone: EditText
    private lateinit var imgProfilePhoto: ShapeableImageView
    private lateinit var btnChangePhoto: TextView
    private var updatedProfilePhotoUrl: String? = null

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
        val v = inflater.inflate(R.layout.fragment_personal_information, container, false)

        val factory = PersonalInformationViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(PersonalInformationViewModel::class.java)

        progressBar = v.findViewById(R.id.progressBar)
        etUserName = v.findViewById(R.id.etUsername)
        etEmail = v.findViewById(R.id.etEmail)
        etName = v.findViewById(R.id.etName)
        etBio = v.findViewById(R.id.etBio)
        etPhone = v.findViewById(R.id.etPhone)
        imgProfilePhoto = v.findViewById(R.id.imgProfilePhoto)
        btnChangePhoto = v.findViewById(R.id.tvChangePhoto)

        val btnBack = v.findViewById<AppCompatButton>(R.id.btnBack)
        btnBack.setOnClickListener{
            backToAccountSettings()
        }

        val btnSave = v.findViewById<AppCompatButton>(R.id.btnSave)
        btnSave.setOnClickListener{
            progressBar!!.visibility = View.VISIBLE
            saveUserPersonalInfo()
        }

        btnChangePhoto.setOnClickListener {
            val iGalery = Intent(Intent.ACTION_GET_CONTENT)
            iGalery.setType("image/*")
            startActivityForImagePicker.launch(iGalery)
        }

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToUserData()
        viewModel.dataUpdateState.observe(this) { state ->
            when (state) {
                is PersonalInformationViewModel.DataUpdateState.Success -> {
                    showShortToast("User data updated successfully")
                    progressBar!!.visibility = View.GONE
                    backToAccountSettings()
                }
                is PersonalInformationViewModel.DataUpdateState.Failure -> {
                    showShortToast("Failed to update user data: ${state.exception.message}")
                    // Handle the error
                    progressBar!!.visibility = View.GONE
                }
            }
        }
    }

    private fun saveUserPersonalInfo(){
        //I think we can introduce data validation here
        if(updatedProfilePhotoUrl != null){
            viewModel.updateUserData(
                etUserName.text.toString(),
                etEmail.text.toString(),
                etName.text.toString(),
                etBio.text.toString(),
                etPhone.text.toString(),
                updatedProfilePhotoUrl!!
            )
        }
        else {
            viewModel.updateUserData(
                etUserName.text.toString(),
                etEmail.text.toString(),
                etName.text.toString(),
                etBio.text.toString(),
                etPhone.text.toString()
            )
        }
    }

    private fun subscribeToUserData(){
        progressBar!!.visibility = View.VISIBLE
        viewModel.getUser()
        viewModel.user.observe(viewLifecycleOwner) { user ->
            etUserName.setText(user.username)
            etEmail.setText(user.email)
            etName.setText(user.name)
            etBio.setText(user.bio)
            etPhone.setText(user.phoneNumber)

            GlideApp.with(this)
                .asBitmap()
                .load(user.photoUrl)
                .apply(
                    RequestOptions
                        .circleCropTransform().placeholder(R.drawable.photo2)
                        .error(R.drawable.photo2)
                )
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        imgProfilePhoto.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        imgProfilePhoto.setImageDrawable(placeholder)
                    }
                })
            progressBar!!.visibility = View.GONE
        }
    }

    var startActivityForImagePicker = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val data = result.data
            // processing the data
            if (data != null
                && data.data != null
            ) {
                val selectedImageUri: Uri? = data.data
                Log.d(TAG, "selected image URI - ".plus(selectedImageUri.toString()))
                val selectedImageBitmap: Bitmap
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        selectedImageUri)
                    setSelectedImage(selectedImageBitmap, selectedImageUri)

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun setSelectedImage(selectedImageBitmap: Bitmap, selectedImageUri: Uri?){
        imgProfilePhoto.setImageBitmap(selectedImageBitmap)
        selectedImageUri?.let { updatedProfilePhotoUrl = it.toString() }
    }

    private fun backToAccountSettings(){
        val navHostFragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navHostFragment.navController.navigate(R.id.action_nav_personal_info_to_nav_account_settings)
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
         * @return A new instance of fragment PersonalInformationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PersonalInformationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        private const val TAG = "PERSONAL_INFORMATION_FRAGMENT_TAG"
    }
}