package com.example.pollista.UI.NavigationFragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.pollista.Adapters.GridViewAdapter
import com.example.pollista.UI.ExternalResources.SpannedGridLayoutManager
import com.example.pollista.UI.ExternalResources.SpannedGridLayoutManager.SpanInfo
import com.example.pollista.ViewModel.SearchViewModel
import com.example.pollista.ViewModelFactory.SearchViewModelFactory
import com.example.projectlab_pollista.R
import kotlinx.coroutines.launch


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewModel: SearchViewModel
    private lateinit var exploreViewAdapter: GridViewAdapter

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
        val v = inflater.inflate(R.layout.fragment_search, container, false)

        val title = v.findViewById<TextView>(R.id.tvDiscoverLabel)
        val typeFace = Typeface.create(
            ResourcesCompat.getFont(requireActivity().applicationContext,R.font.gilroymedium),
            Typeface.NORMAL)
        title.typeface = typeFace

        val factory = SearchViewModelFactory()

        viewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)

        val recyclerView = v.findViewById<RecyclerView>(R.id.recyclerView)
        var bigItemToCheck = 0
        var bigOnLeft = true
        val manager = SpannedGridLayoutManager(
            object : SpannedGridLayoutManager.GridSpanLookup {
                override fun getSpanInfo(position: Int): SpanInfo {
                    // Conditions for 2x2 items
                    return if (position == bigItemToCheck) {
                        if(bigOnLeft) {
                            bigItemToCheck += 10
                            bigOnLeft = false
                        }
                        else {
                            bigItemToCheck += 8
                            bigOnLeft=true
                        }
                        SpanInfo(2, 2)
                    } else {
                        SpanInfo(1, 1)
                    }
                }
            },
            3,  // number of columns
            0.556f // how big is default item
        )

        exploreViewAdapter = GridViewAdapter()

        recyclerView.layoutManager = manager
        recyclerView.adapter = exploreViewAdapter
        return v;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recommendedPosts.asFlow().collect{
                viewLifecycleOwner.lifecycleScope.launch{
                    exploreViewAdapter.submitData(it)
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}