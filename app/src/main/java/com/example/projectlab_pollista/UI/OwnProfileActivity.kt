package com.example.projectlab_pollista.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.projectlab_pollista.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class OwnProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setContentView(R.layout.activity_own_profile)
        var navigationView: BottomNavigationView = findViewById(R.id.nav_view)
        navigationView.itemIconTintList = null
        var fab: FloatingActionButton = findViewById(R.id.fab_button)
        fab.imageTintList = null

    }
}