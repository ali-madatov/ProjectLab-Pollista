package com.example.pollista.Adapters

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pollista.ExternalResources.CustomTypefaceSpan
import com.example.pollista.Model.NotificationModel
import com.example.projectlab_pollista.R
import com.google.android.material.imageview.ShapeableImageView

class NotificationsAdapter(var context: Context): RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    var dataList = emptyList<NotificationModel>()

    internal fun setDataList(dataList: List<NotificationModel>){
        this.dataList = dataList
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var userImageView: ShapeableImageView
        var description: TextView
        var postImage1: ImageView
        var postImage2: ImageView

        init {
            userImageView = itemView.findViewById(R.id.sivUserProfilePhoto)
            description = itemView.findViewById(R.id.tvNotificationDescription)
            postImage1 = itemView.findViewById(R.id.first_image)
            postImage2 = itemView.findViewById(R.id.second_image)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]

        holder.userImageView.setImageResource(data.userImage)

        val typeFace1 = Typeface.create(ResourcesCompat.getFont(context,R.font.gilorysemibold),Typeface.BOLD)
        val typeFace2 = Typeface.create(ResourcesCompat.getFont(context,R.font.giloryregular),Typeface.NORMAL)
        val spannable = SpannableString(data.userName+" "+data.action+"  "+data.timeElapsed)

        //setting typefaces
        spannable.setSpan(CustomTypefaceSpan("",typeFace1),
                                    0,data.userName.length,
                                    Spannable.SPAN_INCLUSIVE_INCLUSIVE)

        spannable.setSpan(CustomTypefaceSpan("",typeFace2),
            data.userName.length+1, spannable.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE)

        //setting colors
        spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(context,R.color.main_black)),
                    0,spannable.length - data.timeElapsed.length,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

        spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(context,R.color.color_divider_dark)),
            spannable.length - data.timeElapsed.length, spannable.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

        //setting sizes
        spannable.setSpan(RelativeSizeSpan(0.8f),
                spannable.length - data.timeElapsed.length, spannable.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        holder.description.text = spannable

        if(data.postImage1!=null && data.postImage2!=null){
            holder.postImage1.setImageResource(data.postImage1!!)
            holder.postImage2.setImageResource(data.postImage2!!)
        }

    }

    override fun getItemCount() = dataList.size
}