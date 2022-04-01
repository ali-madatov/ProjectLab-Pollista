package com.example.projectlab_pollista.UI

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.projectlab_pollista.Adapters.GridViewAdapter
import com.example.projectlab_pollista.GlideApp
import com.example.projectlab_pollista.Model.PostModel
import com.example.projectlab_pollista.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val imageUrl = "https://static.remove.bg/remove-bg-web/b27c50a4d669fdc13528397ba4bc5bd61725e4cc/assets/start_remove-c851bdf8d3127a24e2d137a55b1b427378cd17385b01aec6e59d5d4b5f39d2ec.png";
        super.onCreate(savedInstanceState)

        setWindowDecorview(true)

        setContentView(R.layout.activity_account)
        val fab = findViewById<FloatingActionButton>(R.id.fab_button)
        fab.imageTintList = null

        var bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        val menu: Menu = bottomNavigationView.menu
        val menuItem: MenuItem = menu.findItem(R.id.nav_profile)
        //loading profile picture from url using Glide Library
        GlideApp.with(this)
            .asBitmap()
            .load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
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

    fun onNavigationViewColorChanged(navView: BottomNavigationView,fab: FloatingActionButton,isDarkMode: Boolean){
        setWindowDecorview(isDarkMode)
        if(isDarkMode){
            navView.setBackgroundResource(R.drawable.black_panel)
            var menu = navView.menu
            menu.getItem(0).setIcon(R.drawable.white_home_icon)
            menu.getItem(1).setIcon(R.drawable.white_search_icon)
            fab.setImageResource(R.drawable.ic_add)
            menu.getItem(3).setIcon(R.drawable.white_tick_icon)
        }
        else{
            navView.setBackgroundResource(R.drawable.navigation_panel)
            var menu = navView.menu
            menu.getItem(0).setIcon(R.drawable.selector_homeicon)
            menu.getItem(1).setIcon(R.drawable.selector_searchicon)
            fab.setImageResource(R.drawable.ic_blue_addicon)
            menu.getItem(3).setIcon(R.drawable.selector_tickicon)
        }

    }
    fun setWindowDecorview(isDarkMode: Boolean){

            //hiding navigation bar
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

            //making status bar transparent and stick layout to full screen
            if(Build.VERSION.SDK_INT>=30){
                if(isDarkMode) {
                    window.statusBarColor = Color.TRANSPARENT
                    window.insetsController?.setSystemBarsAppearance(
                        0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                }
                else
                    window.insetsController?.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                // Making status bar overlaps with the activity
                WindowCompat.setDecorFitsSystemWindows(window, !isDarkMode)
            }


    }

}