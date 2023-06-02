package com.example.pollista.UI.NavigationFragments.BottomSheets

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import com.example.pollista.UI.SignInActivity
import com.example.projectlab_pollista.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OwnProfileModalBottomSheet: BottomSheetDialogFragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.bottomsheet_own_profile,container,false)
        val navHostFragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val btnAppSettings = v.findViewById<TextView>(R.id.ctvAppSettings)
        btnAppSettings.setOnClickListener{
            navHostFragment.navController.navigate(R.id.action_nav_profile_to_nav_app_settings)
            dismiss()
        }

        val btnAccountSettings = v.findViewById<TextView>(R.id.ctvAccountSettings)
        btnAccountSettings.setOnClickListener{
            navHostFragment.navController.navigate(R.id.action_nav_profile_to_nav_account_settings)
            dismiss()
        }

        val btnLogout = v.findViewById<TextView>(R.id.ctvLogout)
        btnLogout.setOnClickListener{
            startActivity(Intent(requireActivity(), SignInActivity::class.java))
        }
        return v
    }

    companion object{
        const val TAG = "OwnProfileModalBottomSheet"
    }

    private fun dpToPx(dp: Int): Int {
        // https://developer.android.com/guide/practices/screens_support.html#dips-pels
        val density: Float = Resources.getSystem().getDisplayMetrics().density
        return (dp * density + 0.5f).toInt()
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme


}