package com.example.pollista.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.projectlab_pollista.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setContentView(R.layout.activity_sign_in)

        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")
    }

    fun signUpNeeded( view: View){
        val intent = Intent(this,SignUpActivity::class.java)
        startActivity(intent)
    }

    fun signUpWithGoogle(view: View){

    }

    fun backToMainPage(view: View){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    fun forgottenPassword(view: View){
        val intent = Intent(this,ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    fun signIn(view: View){
        val etUsername: EditText = findViewById(R.id.etUsername)
        val etPassword: EditText = findViewById(R.id.etPassword)
        when{
            TextUtils.isEmpty(etUsername.text.toString().trim{ it <= ' '}) -> {
                Toast.makeText(
                    this@SignInActivity,
                    "Please enter email",
                    Toast.LENGTH_SHORT
                ).show()
            }

            TextUtils.isEmpty(etPassword.text.toString().trim{ it <= ' '}) -> {
                Toast.makeText(
                    this@SignInActivity,
                    "Please enter password",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {
                val email: String = etUsername.text.toString().trim { it <= ' '}
                val password: String = etPassword.text.toString().trim { it <= ' '}

                FirebaseAuth.getInstance()
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            Toast.makeText(
                                this@SignInActivity,
                                "Logged in successfully!",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(this@SignInActivity, AccountActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
                            intent.putExtra("email_id", email)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@SignInActivity,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            }

        }
    }
}