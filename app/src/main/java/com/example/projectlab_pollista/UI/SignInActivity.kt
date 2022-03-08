package com.example.projectlab_pollista.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.projectlab_pollista.MainActivity
import com.example.projectlab_pollista.R

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setContentView(R.layout.activity_sign_in)
    }

    fun signUpNeeded( view: View){
        val intent = Intent(this,SignUpActivity::class.java)
        startActivity(intent)
    }

    fun backToMainPage(view: View){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}