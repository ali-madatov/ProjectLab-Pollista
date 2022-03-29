package com.example.projectlab_pollista.UI

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
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

    private lateinit var gridViewAdapter: GridViewAdapter
    private var dataList = mutableListOf<PostModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        val imageUrl = "https://static.remove.bg/remove-bg-web/b27c50a4d669fdc13528397ba4bc5bd61725e4cc/assets/start_remove-c851bdf8d3127a24e2d137a55b1b427378cd17385b01aec6e59d5d4b5f39d2ec.png";
        super.onCreate(savedInstanceState)
        //hiding navigation bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        //making status bar transparent and stick layout to full screen
        if (Build.VERSION.SDK_INT in 21..29) {
            window.statusBarColor = Color.TRANSPARENT
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility =
                SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE

        } else if (Build.VERSION.SDK_INT >= 30) {
            window.statusBarColor = Color.TRANSPARENT
            // Making status bar overlaps with the activity
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }


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
                    TODO("Not yet implemented")
                }
            })


        bottomNavigationView.itemIconTintList = null

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener {  _, destination, _ ->
            if(destination.id==R.id.nav_home){
                bottomNavigationView.setBackgroundResource(R.drawable.black_panel)
            }
            else
                bottomNavigationView.setBackgroundResource(R.drawable.navigation_panel)
        }

        bottomNavigationView.setupWithNavController(navController)


    }

}