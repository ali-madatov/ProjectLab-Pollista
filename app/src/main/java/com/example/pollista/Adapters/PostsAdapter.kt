package com.example.pollista.Adapters

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.pollista.BusinessModel.HomePostDetails
import com.example.pollista.Modules.GlideApp
import com.example.pollista.UI.NavigationFragments.OnVoteClickListener
import com.example.projectlab_pollista.R
import com.google.android.material.imageview.ShapeableImageView
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup


class PostsAdapter(private val onVoteClickListener: OnVoteClickListener) : PagingDataAdapter<HomePostDetails, PostsAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HomePostDetails>() {
            override fun areItemsTheSame(oldItem: HomePostDetails, newItem: HomePostDetails): Boolean =
                oldItem.postID == newItem.postID

            override fun areContentsTheSame(oldItem: HomePostDetails, newItem: HomePostDetails): Boolean =
                oldItem == newItem
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image1: ImageView = itemView.findViewById(R.id.imgFirstImage)
        var image2: ImageView = itemView.findViewById(R.id.imgSecondImage)
        var userPhoto: ShapeableImageView = itemView.findViewById(R.id.sivUserProfilePhoto)
        var username: TextView = itemView.findViewById(R.id.tvUsername)
        var description: TextView = itemView.findViewById(R.id.tvDescription)
        var tags: TextView = itemView.findViewById(R.id.tvTags)
        var buttonGroup: ThemedToggleButtonGroup = itemView.findViewById(R.id.grpToggleButtons)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)

        data?.let { it ->
            holder.buttonGroup.setOnSelectListener { themedButton: ThemedButton ->
                val isUpvote = when (themedButton.id) {
                    R.id.btnUpVote -> true
                    R.id.btnDownVote -> false
                    else -> throw IllegalStateException("Unexpected button ID")
                }
                onVoteClickListener.onVoteClick(it.postID, isUpvote)
            }

            GlideApp.with(holder.itemView.context)
                .load(it.firstImageUri)
                .into(holder.image1)

            GlideApp.with(holder.itemView.context)
                .load(it.secondImageUri)
                .into(holder.image2)
            GlideApp.with(holder.itemView.context)
                .asBitmap()
                .load(it.userCoreDetails.photoUrl)
                .apply(
                    RequestOptions
                        .circleCropTransform().placeholder(R.drawable.photo2).error(R.drawable.photo2))
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        holder.userPhoto.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        holder.userPhoto.setImageDrawable(placeholder)
                    }
                })
            holder.username.text = it.userCoreDetails.username
            holder.description.text = it.postDescription.caption
            holder.tags.text = it.postDescription.tags.joinToString(" ")
            it.voterDetails.optionOneVoted?.let { isUpVote ->
                if(isUpVote){
                    holder.buttonGroup.selectButton(R.id.btnUpVote)
                }
                else{
                    holder.buttonGroup.selectButton(R.id.btnDownVote)
                }

            }
        }
    }

}
