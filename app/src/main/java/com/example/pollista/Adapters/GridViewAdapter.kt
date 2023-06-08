package com.example.pollista.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pollista.DataAccess.Model.PostModel
import com.example.pollista.Modules.GlideApp
import com.example.projectlab_pollista.R

class GridViewAdapter : PagingDataAdapter<PostModel, GridViewAdapter.ViewHolder>(
    DIFF_CALLBACK
) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PostModel>() {
            override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean =
                oldItem.postID == newItem.postID

            override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean =
                oldItem == newItem
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var image1: ImageView = itemView.findViewById(R.id.first_image)
        var image2: ImageView = itemView.findViewById(R.id.second_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridViewAdapter.ViewHolder, position: Int) {
        val data = getItem(position)

        data?.let {
            GlideApp.with(holder.itemView.context)
                .load(it.image1)
                .into(holder.image1)

            GlideApp.with(holder.itemView.context)
                .load(it.image2)
                .into(holder.image2)
        }

    }

}