package com.example.pollista.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.pollista.ViewModel.ForgotPasswordViewModel
import com.example.pollista.ViewModelFactory.ForgotPasswordViewModelFactory
import com.example.projectlab_pollista.R

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var viewModel: ForgotPasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setContentView(R.layout.activity_forgot_password)

        val factory = ForgotPasswordViewModelFactory("")
        viewModel = ViewModelProvider(this, factory).get(ForgotPasswordViewModel::class.java)
    }

    fun backToMainPage(view: View){
        var intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}