package com.example.pollista.UI

import android.app.ProgressDialog
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
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import com.example.projectlab_pollista.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.util.*

class AddPostActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var storageReference: StorageReference
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

        progressBar = findViewById<ProgressBar>(R.id.progressBar) as ProgressBar

        val btnBack = findViewById<AppCompatButton>(R.id.btnBack)
        btnBack.setOnClickListener{
            backToAccountPage()
        }

        val btnShare = findViewById<Button>(R.id.btnShare)
        btnShare.setOnClickListener{
            sharePost()
        }
        val btnAddFirstImage = findViewById<Button>(R.id.btnAddFirstImage)
        val btnAddSecondImage = findViewById<Button>(R.id.btnAddSecondImage)

        btnAddFirstImage.setOnClickListener{
            isFirstImage=true
            val iGalery = Intent(Intent.ACTION_GET_CONTENT)
            iGalery.setType("image/*")
            startActivityForImagePicker.launch(iGalery)
        }

        btnAddSecondImage.setOnClickListener{
            isFirstImage=false
//            val iGalery = Intent(Intent.ACTION_PICK)
//            iGalery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            val iGalery = Intent(Intent.ACTION_GET_CONTENT)
            iGalery.setType("image/*")
            startActivityForImagePicker.launch(iGalery)
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

    private fun sharePost(){
        progressBar!!.visibility = View.VISIBLE
        val firebaseUser = auth.currentUser
        val userUid = firebaseUser!!.uid
        val postId = UUID.randomUUID().toString()
        val child = "users/".plus(userUid).plus("/").plus(postId).plus("/images/")
        storageReference = FirebaseStorage.getInstance().getReference(child).child("firstImage.jpg")
        try {
            storageReference.putFile(firstImage!!)
                .addOnSuccessListener {
                    storageReference.parent!!.child("secondImage.jpg").putFile(secondImage!!)
                        .addOnSuccessListener {
                            Log.d(TAG, "AddPostActivity: Successfully uploaded images!")
                            Toast.makeText(this@AddPostActivity, "Successfully uploaded images!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener{
                            Log.d(TAG, "AddPostActivity: Failed to upload the second image!")
                            Toast.makeText(this@AddPostActivity, "Failed to upload the second image!", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener{
                    Log.d(TAG, "AddPostActivity: Failed to upload the first image!")
                    Toast.makeText(this@AddPostActivity, "Failed to upload the first image!", Toast.LENGTH_SHORT).show()
                }
                .addOnCompleteListener{
                    progressBar!!.visibility = View.GONE
                }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
        /*
            Post specifications will be stored here
         */
        val intent = Intent(this,AccountActivity::class.java)
        startActivity(intent)
    }
}