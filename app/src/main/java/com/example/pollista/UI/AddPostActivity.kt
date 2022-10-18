package com.example.pollista.UI

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import com.example.projectlab_pollista.R
import java.io.IOException

class AddPostActivity : AppCompatActivity() {

    private var isFirstImage: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        setWindowDecorView()

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
            val iGalery = Intent(Intent.ACTION_PICK)
            iGalery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForImagePicker.launch(iGalery)
        }

        btnAddSecondImage.setOnClickListener{
            isFirstImage=false
            val iGalery = Intent(Intent.ACTION_PICK)
            iGalery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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
                val selectedImageBitmap: Bitmap
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                                                                    this.contentResolver,
                                                                    selectedImageUri)
                    setSelectedImage(selectedImageBitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun setSelectedImage(selectedImageBitmap: Bitmap){
        if(isFirstImage==true){
            val firstImageView = findViewById<ImageView>(R.id.first_image)
            firstImageView.setImageBitmap(selectedImageBitmap)
        }
        else if(isFirstImage==false){
            val secondImageView =  findViewById<ImageView>(R.id.second_image)
            secondImageView.setImageBitmap(selectedImageBitmap)
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
        /*
            Post specifications will be stored here
         */
        val intent = Intent(this,AccountActivity::class.java)
        startActivity(intent)
    }
}