package com.example.pollista.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import com.example.pollista.ViewModel.AddPostViewModel
import com.example.pollista.ViewModel.SignUpViewModel
import com.example.pollista.ViewModelFactory.SignUpViewModelFactory
import com.example.projectlab_pollista.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.Exception

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var viewModel: SignUpViewModel
    private var progressBar: ProgressBar? = null

    //constants
    private companion object{
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setContentView(R.layout.activity_sign_up)

        val factory = SignUpViewModelFactory("")
        viewModel = ViewModelProvider(this, factory).get(SignUpViewModel::class.java)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        auth = FirebaseAuth.getInstance()
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        checkUser()

        val signUpWithGoogleBtn = findViewById<AppCompatButton>(R.id.btnGoogleSignUp)
        signUpWithGoogleBtn.setOnClickListener{
            progressBar!!.visibility = View.VISIBLE
            signUpWithGoogle(it)
        }

        // Observe the post upload state
        viewModel.userAddState.observe(this) { state ->
            Log.d(TAG, "Saving the user data...")
            when (state) {
                is SignUpViewModel.UserAddState.Success -> {
                    showShortToast("Registration has been successfully completed!")
                    progressBar!!.visibility = View.GONE
                    //TODO: check where to navigate based on method
                    val intent = Intent(this@SignUpActivity, AccountActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is SignUpViewModel.UserAddState.Failure -> {
                    showShortToast(state.exception.message.toString())
                    // Handle the error
                    progressBar!!.visibility = View.GONE
                }
            }
        }

    }

    private fun checkUser() {
        val firebaseUser = auth.currentUser
//        if (firebaseUser != null){
//            startActivity(Intent(this@SignUpActivity, AccountActivity::class.java))
//            finish()
//        }
    }

    fun signUpWithGoogle(view: View){
        Log.d(TAG, "SignUpActivity: begin Google SignIn")
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            Log.d(TAG, "onActivityResult: Google SignIn intent result")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                // Google SignIn success, now auth with firebase
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)
            }
            catch (e: Exception){
                Log.d(TAG, "onActivityResult: ${e.message}")
            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account")

        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                Log.d(TAG, "firebaseAuthWithGoogleAccount: LoggedIn")

                val firebaseUser = auth.currentUser

                val uid = firebaseUser!!.uid
                val email = firebaseUser!!.email

                Log.d(TAG, "firebaseAuthWithGoogleAccount: Uid: $uid")
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Email: $email")

                //check if the user is new or existing
                if(authResult.additionalUserInfo!!.isNewUser){
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Account created... \n$email")
                    viewModel.saveUserData(uid, email)
                    //Toast.makeText(this@SignUpActivity, "Account created... \n$email", Toast.LENGTH_SHORT).show()
                }
                else{
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Existing user... \n$email")
                    val intent = Intent(this@SignUpActivity, AccountActivity::class.java)
                    startActivity(intent)
                    finish()
                    //Toast.makeText(this@SignUpActivity, "LoggedIn... \n$email", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{  e ->
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Login failed due to ${e.message}")
                showShortToast("Login failed due to ${e.message}")
            }
    }

    fun signUpWithCustom(view: View){
        val etEmailAddress = findViewById<EditText>(R.id.etEmailAddress)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etCnfrmPassword = findViewById<EditText>(R.id.etCnfrmPassword)
        when{
            TextUtils.isEmpty(etEmailAddress.text.toString().trim{ it <= ' '}) -> {
                showShortToast("Please enter email")
            }

            TextUtils.isEmpty(etUsername.text.toString().trim{ it <= ' '}) -> {
                showShortToast("Please enter username")
            }

            TextUtils.isEmpty(etPassword.text.toString().trim{ it <= ' '}) -> {
                showShortToast("Please enter password")
            }

             etPassword.text.toString() != etCnfrmPassword.text.toString() -> {
                 showShortToast("Passwords do not match")
            }
            else -> {
                val email: String = etEmailAddress.text.toString().trim { it <= ' '}
                val username: String = etUsername.text.toString().trim { it <= ' '}
                val password: String = etPassword.text.toString().trim { it <= ' '}

                FirebaseAuth.getInstance()
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            viewModel.saveUserData(
                                firebaseUser.uid,
                                email,
                                username,
                                password
                            )
                        } else {
                            Toast.makeText(
                                this@SignUpActivity,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            }

        }
    }

    fun backToGetStarted(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun signInNeeded(view: View){
        val intent = Intent(this,SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showShortToast(message: String){
        Toast.makeText(
            this@SignUpActivity,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}