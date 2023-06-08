package com.example.pollista.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.pollista.ViewModel.EmailConfirmViewModel
import com.example.pollista.ViewModelFactory.EmailConfirmViewModelFactory
import com.example.projectlab_pollista.R

class EmailConfirmActivity : AppCompatActivity() {

    lateinit var viewModel: EmailConfirmViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        setContentView(R.layout.activity_email_confirm)

        val factory = EmailConfirmViewModelFactory("")
        viewModel = ViewModelProvider(this, factory).get(EmailConfirmViewModel::class.java)
    }
}