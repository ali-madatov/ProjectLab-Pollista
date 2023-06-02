package com.example.pollista.UI

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import com.example.pollista.ViewModel.AddPostViewModel
import com.example.pollista.ViewModelFactory.AddPostViewModelFactory
import com.example.projectlab_pollista.R
import com.google.firebase.auth.FirebaseAuth
import java.io.IOException

class AddPostActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: AddPostViewModel
    private var firstImage: Uri? = null
    private var secondImage: Uri? = null
    private var isFirstImage: Boolean? = null

    private var progressBar: ProgressBar? = null

    //constants
    private companion object{
        private const val TAG = "ADD_POST_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        setWindowDecorView()

        auth = FirebaseAuth.getInstance()
        progressBar = findViewById(R.id.progressBar)

        val userId = auth.currentUser!!.uid
        val factory = AddPostViewModelFactory(userId)
        viewModel = ViewModelProvider(this, factory).get(AddPostViewModel::class.java)

        // Observe the post upload state
        viewModel.postUploadState.observe(this) { state ->
            when (state) {
                is AddPostViewModel.PostUploadState.Success -> {
                    showShortToast("Post uploaded successfully")
                    progressBar!!.visibility = View.GONE
                    backToAccountPage()
                }
                is AddPostViewModel.PostUploadState.Failure -> {
                    showShortToast("Failed to upload post: ${state.exception.message}")
                    // Handle the error
                    progressBar!!.visibility = View.GONE
                }
            }
        }


        val btnBack = findViewById<AppCompatButton>(R.id.btnBack)
        btnBack.setOnClickListener{
            backToAccountPage()
        }

        val btnAddFirstImage = findViewById<Button>(R.id.btnAddFirstImage)
        val btnAddSecondImage = findViewById<Button>(R.id.btnAddSecondImage)
        val etCaption = findViewById<EditText>(R.id.etCaption)

        btnAddFirstImage.setOnClickListener{
            isFirstImage=true
            val iGalery = Intent(Intent.ACTION_GET_CONTENT)
            iGalery.setType("image/*")
            startActivityForImagePicker.launch(iGalery)
        }

        btnAddSecondImage.setOnClickListener{
            isFirstImage=false
            val iGalery = Intent(Intent.ACTION_GET_CONTENT)
            iGalery.setType("image/*")
            startActivityForImagePicker.launch(iGalery)
        }

        val btnShare = findViewById<Button>(R.id.btnShare)
        btnShare.setOnClickListener {
            hideSoftKeyboard()
            if (firstImage == null || secondImage == null) {
                showShortToast("Both images must be selected")
            } else {
                progressBar!!.visibility = View.VISIBLE
                val caption = etCaption.text.toString()
                viewModel.sharePost(firstImage!!, secondImage!!, caption)
            }
        }
    }

    var startActivityForImagePicker = registerForActivityResult(
                                                ActivityResultContracts.StartActivityForResult()
                                                    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            // processing the data
            if (data != null
                && data.data != null
            ) {
                val selectedImageUri: Uri? = data.data
                Log.d(TAG, "AddActivityPost: selected image URI - ".plus(selectedImageUri.toString()))
                val selectedImageBitmap: Bitmap
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                                                                    this.contentResolver,
                                                                    selectedImageUri)
                    setSelectedImage(selectedImageBitmap, selectedImageUri)

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun setSelectedImage(selectedImageBitmap: Bitmap, selectedImageUri: Uri?){
        if(isFirstImage==true){
            val firstImageView = findViewById<ImageView>(R.id.first_image)
            firstImageView.setImageBitmap(selectedImageBitmap)
            firstImage = selectedImageUri
        }
        else if(isFirstImage==false){
            val secondImageView =  findViewById<ImageView>(R.id.second_image)
            secondImageView.setImageBitmap(selectedImageBitmap)
            secondImage = selectedImageUri
        }
    }

    private fun setWindowDecorView(){
        window.statusBarColor = resources.getColor(R.color.main_black)
        window.navigationBarColor = resources.getColor(R.color.main_black)

        //making status bar transparent and stick layout to full screen
        if(Build.VERSION.SDK_INT>=30){
                window.insetsController?.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
                window.insetsController?.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                )

        }
        else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    private fun backToAccountPage(){
        val intent = Intent(this,AccountActivity::class.java)
        startActivity(intent)
    }

    private fun hideSoftKeyboard(){
        val inputMethodManager: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if(inputMethodManager.isAcceptingText){
            this.currentFocus?.let {
                inputMethodManager.hideSoftInputFromWindow(
                    it.windowToken,
                    0
                )
            }
        }
    }

    private fun showShortToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
