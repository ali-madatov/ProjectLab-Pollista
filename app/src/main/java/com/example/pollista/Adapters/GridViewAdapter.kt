package com.example.pollista.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.pollista.Model.PostModel
import com.example.projectlab_pollista.R

class GridViewAdapter(var context: Context) : RecyclerView.Adapter<GridViewAdapter.ViewHolder>() {

    var dataList = emptyList<PostModel>()

    internal fun setDataList(dataList: List<PostModel>){
        this.dataList = dataList
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var image1: ImageView
        var image2: ImageView

        init {
            image1 = itemView.findViewById(R.id.first_image)
            image2 = itemView.findViewById(R.id.second_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewAdapter.ViewHolder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.post_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridViewAdapter.ViewHolder, position: Int) {
        var data = dataList[position]

        holder.image1.setImageResource(data.image1)
        holder.image2.setImageResource(data.image2)
    }

    override fun getItemCount() = dataList.size
}