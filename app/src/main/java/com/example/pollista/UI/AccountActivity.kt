package com.example.pollista.UI

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.View.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.pollista.Modules.GlideApp
import com.example.pollista.ViewModel.AccountViewModel
import com.example.pollista.ViewModelFactory.AccountViewModelFactory
import com.example.projectlab_pollista.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth


class AccountActivity : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var menuItem: MenuItem
    lateinit var viewModel: AccountViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        setWindowDecorView(true)

        val factory = AccountViewModelFactory(FirebaseAuth.getInstance().currentUser!!.uid)
        viewModel = ViewModelProvider(this, factory).get(AccountViewModel::class.java)

        val fab = findViewById<FloatingActionButton>(R.id.fab_button)
        fab.imageTintList = null
        fab.setOnClickListener { startAddPostActivity() }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        val menu: Menu = bottomNavigationView.menu
        menuItem = menu.findItem(R.id.nav_profile)


        uploadProfileImage()

        bottomNavigationView.itemIconTintList = null

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener {  _, destination, _ ->
            if(destination.id==R.id.nav_home){
                onNavigationViewColorChanged(bottomNavigationView,fab,true)
            }
            else
                onNavigationViewColorChanged(bottomNavigationView,fab,false)
        }

        bottomNavigationView.setupWithNavController(navController)


    }


    private fun startAddPostActivity(){
        val intent = Intent(this,AddPostActivity::class.java)
        startActivity(intent)
    }

    fun uploadProfileImage(){
        viewModel.getProfilePhotoUrl()
        //loading profile picture from url using Glide Library
        viewModel.profilePhotoUrl.observe(this){
            it?.let {imageUrl ->
                GlideApp.with(this)
                    .asBitmap()
                    .load(imageUrl)
                    .apply(
                        RequestOptions
                            .circleCropTransform().placeholder(R.drawable.photo2).error(R.drawable.photo2))
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            menuItem.icon = BitmapDrawable(resources, resource)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
            }
        }
    }

    private fun onNavigationViewColorChanged(navView: BottomNavigationView,fab: FloatingActionButton,isDarkMode: Boolean){
        setWindowDecorView(isDarkMode)
        val menu = navView.menu
        if(isDarkMode){
            navView.setBackgroundResource(R.drawable.black_panel)
            menu.getItem(0).setIcon(R.drawable.white_home_icon)
            menu.getItem(1).setIcon(R.drawable.white_search_icon)
            fab.setImageResource(R.drawable.ic_add)
            menu.getItem(3).setIcon(R.drawable.white_tick_icon)
        }
        else{
            navView.setBackgroundResource(R.drawable.navigation_panel)
            menu.getItem(0).setIcon(R.drawable.selector_homeicon)
            menu.getItem(1).setIcon(R.drawable.selector_searchicon)
            fab.setImageResource(R.drawable.ic_blue_addicon)
            menu.getItem(3).setIcon(R.drawable.selector_tickicon)
        }

    }

    private fun setWindowDecorView(isDarkMode: Boolean){


            //making status bar transparent and stick layout to full screen
            if(Build.VERSION.SDK_INT>=30){
                if(isDarkMode) {
                    window.statusBarColor = resources.getColor(R.color.black)
                    window.navigationBarColor = resources.getColor(R.color.black)
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
                    window.statusBarColor = resources.getColor(R.color.white)
                    window.navigationBarColor = resources.getColor(R.color.white)
                    window.insetsController?.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                    window.insetsController?.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                    )
                }

            }
            else {
                if(isDarkMode){
                    window.statusBarColor = resources.getColor(R.color.black)
                    window.navigationBarColor = resources.getColor(R.color.black)
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    }
                else{
                    window.statusBarColor = resources.getColor(R.color.white)
                    window.navigationBarColor = resources.getColor(R.color.white)
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

                }
            }


    }

}