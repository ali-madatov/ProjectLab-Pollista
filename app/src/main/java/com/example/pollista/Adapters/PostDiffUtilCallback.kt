package com.example.pollista.Adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.pollista.DataAccess.Model.PostModel

class PostDiffUtilCallback : DiffUtil.ItemCallback<PostModel>() {
    override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
        // TODO: Replace with your own logic
        return oldItem.postID == newItem.postID
    }

    override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
        // TODO: Replace with your own logic
        return oldItem == newItem
    }
}
