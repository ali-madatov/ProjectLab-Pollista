package com.example.pollista.UI.NavigationFragments

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pollista.Adapters.NotificationsAdapter
import com.example.pollista.Model.NotificationModel
import com.example.projectlab_pollista.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TickFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TickFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var notificationsAdapter: NotificationsAdapter
    private var dataList = mutableListOf<NotificationModel>()

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
        val v = inflater.inflate(R.layout.fragment_tick, container, false)

        val title = v.findViewById<TextView>(R.id.tvNotifications)
        val typeFace = Typeface.create(
            ResourcesCompat.getFont(requireActivity().applicationContext,R.font.gilroymedium),
            Typeface.NORMAL)
        title.typeface = typeFace

        val recyclerView = v.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity().applicationContext,LinearLayoutManager.VERTICAL,false)
        notificationsAdapter = NotificationsAdapter(requireActivity().applicationContext)
        recyclerView.adapter = notificationsAdapter


        dataList.add(NotificationModel(R.drawable.photo1,"najaf.mahammad","started following you.",
                "22m",null,null))
        dataList.add(NotificationModel(R.drawable.photo2,"jonathan.agner",
                    "voted your post. Click and see his choice!",
                    "26m",R.drawable.image1,R.drawable.image2))
        dataList.add(NotificationModel(R.drawable.photo2,"jonathan.agner",
                    "started following you.",
                    "1h",null,null))
        dataList.add(NotificationModel(R.drawable.photo1,"yunis.mikayilov",
            "voted your post. Click and see his choice!",
            "1h",R.drawable.image2,R.drawable.image1))
        dataList.add(NotificationModel(R.drawable.photo2,"yunis.mikayilov",
            "voted your post. Click and see his choice!",
            "1h",R.drawable.image1,R.drawable.image2))
        dataList.add(NotificationModel(R.drawable.photo1,"yunis.mikayilov",
            "voted your post. Click and see his choice!",
            "1h",R.drawable.image2,R.drawable.image1))
        dataList.add(NotificationModel(R.drawable.photo2,"yunis.mikayilov",
            "voted your post. Click and see his choice!",
            "1h",R.drawable.image1,R.drawable.image2))
        dataList.add(NotificationModel(R.drawable.photo1,"yunis.mikayilov",
            "voted your post. Click and see his choice!",
            "1h",R.drawable.image2,R.drawable.image1))
        dataList.add(NotificationModel(R.drawable.photo1,"yunis.mikayilov",
            "voted your post. Click and see his choice!",
            "1h",R.drawable.image1,R.drawable.image2))
        dataList.add(NotificationModel(R.drawable.photo2,"yunis.mikayilov",
            "started following you.",
            "2h",null,null))

        notificationsAdapter.setDataList(dataList)

        return v
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TickFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TickFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}