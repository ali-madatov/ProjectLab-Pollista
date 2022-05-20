package com.example.projectlab_pollista.UI.NavigationFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.DialogFragment.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectlab_pollista.Adapters.GridViewAdapter
import com.example.projectlab_pollista.Model.PostModel
import com.example.projectlab_pollista.R
import com.example.projectlab_pollista.UI.NavigationFragments.BottomSheets.OwnProfileModalBottomSheet
import com.example.projectlab_pollista.UI.SignInActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var gridViewAdapter: GridViewAdapter
    private var dataList = mutableListOf<PostModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_profile, container, false)

        val buttonHamburger = v.findViewById<AppCompatButton>(R.id.btnHamburger)
        buttonHamburger.setOnClickListener{
            val ownProfileModalBottomSheet = OwnProfileModalBottomSheet()
            ownProfileModalBottomSheet.show(parentFragmentManager,"OwnProfileModalBottomSheet")
        }

        val buttonBack = v.findViewById<AppCompatButton>(R.id.btnBack)
        buttonBack.setOnClickListener{
            backToSignInPage()
        }
        val recyclerView = v.findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = GridLayoutManager(requireActivity().applicationContext,3)
        gridViewAdapter = GridViewAdapter(requireActivity().applicationContext)
        recyclerView.adapter = gridViewAdapter

        for (range in 0..25){
            dataList.add(PostModel(1234567,R.drawable.image1,R.drawable.image2,"Help me to make the right choice :)",
                Arrays.asList("#apple","#samsung","#12pro","#s21ultra")))
        }

        gridViewAdapter.setDataList(dataList)
        return v
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun backToSignInPage(){
        val intent = Intent(requireActivity(),SignInActivity::class.java)
        startActivity(intent)
    }
}