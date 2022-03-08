package com.example.projectlab_pollista

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Contacts
import android.view.View
import com.example.projectlab_pollista.UI.SignUpActivity

const val EXTRA_MESSAGE = "com.example.projectlab_pollista.MESSAGE"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        setContentView(R.layout.activity_getstarted)

    }

    fun signUp(view: View){
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}