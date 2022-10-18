package com.example.pollista.UI.NavigationFragments.BottomSheets

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.projectlab_pollista.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OwnProfileModalBottomSheet: BottomSheetDialogFragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottomsheet_own_profile,container,false)

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