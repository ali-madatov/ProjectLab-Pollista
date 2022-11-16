package com.example.pollista.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pollista.Model.PostModel
import com.example.pollista.Modules.GlideApp
import com.example.pollista.ViewModel.AddPostViewModel
import com.example.projectlab_pollista.R
import com.google.firebase.storage.StorageReference

class PostsAdapter(val viewModel: AddPostViewModel): RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    lateinit var currentContext: Context
    var dataList = viewModel.getRepository().getAllPostModels()

    internal fun setDataList(dataList: List<PostModel>){
        this.dataList = dataList
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var image1: ImageView
        var image2: ImageView
        var description: TextView
        var tags: TextView

        init {
            image1 = itemView.findViewById(R.id.imgFirstImage)
            image2 = itemView.findViewById(R.id.imgSecondImage)
            description = itemView.findViewById(R.id.tvDescription)
            tags = itemView.findViewById(R.id.tvTags)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.home_post,parent,false)
        currentContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]

        //TODO implement binding here
        GlideApp.with(currentContext)
            .load(data.image1)
            .into(holder.image1)
        GlideApp.with(currentContext)
            .load(data.image2)
            .into(holder.image2)
        holder.description.text = data.description
        holder.tags.text = data.tags.joinToString(" ")
//        holder.image1.setImageResource(data.image1)
//        holder.image2.setImageResource(data.image2)
//        holder.description.text = data.description
//        holder.tags.text = data.tags.joinToString(" ")
    }

    override fun getItemCount() = dataList.size
}