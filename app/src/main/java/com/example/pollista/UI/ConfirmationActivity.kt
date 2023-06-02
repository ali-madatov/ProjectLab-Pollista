package com.example.pollista.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.pollista.ViewModel.ConfirmationViewModel
import com.example.pollista.ViewModelFactory.ConfirmationViewModelFactory
import com.example.projectlab_pollista.R

class ConfirmationActivity : AppCompatActivity() {

    lateinit var viewModel: ConfirmationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        setContentView(R.layout.activity_confirmation)

        val factory = ConfirmationViewModelFactory("")
        viewModel = ViewModelProvider(this, factory).get(ConfirmationViewModel::class.java)
    }

    fun signIn(view: View){
        val intent = Intent(this,SignInActivity::class.java)
        startActivity(intent)
    }
}