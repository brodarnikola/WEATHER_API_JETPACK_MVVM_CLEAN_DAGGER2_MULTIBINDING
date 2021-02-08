package com.vjezba.weatherapi.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vjezba.domain.model.youtube.YoutubeVideos
import com.vjezba.weatherapi.R
import com.vjezba.weatherapi.ui.activities.YoutubeWeatherActivity
import kotlinx.android.synthetic.main.youtube_videos_list.view.*

class YoutubeVideosAdapter(
    var youtubeVideosList: MutableList<YoutubeVideos>,
    var mFragmentActivity: FragmentActivity
) : RecyclerView.Adapter<YoutubeVideosAdapter.ViewHolder>() {

    private var isLoaderVisible = false

    var fragmentActivity: FragmentActivity = mFragmentActivity

    enum class ITEM_TYPES(val typeValue: Int) {
        VIEW_TYPE_VIDEOS(0),
        VIEW_TYPE_LOADING(1);

        companion object {
            fun from(findViewByIdValue: Int): ITEM_TYPES =
                values().first { it.typeValue == findViewByIdValue }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            if (position == youtubeVideosList.size - 1) ITEM_TYPES.VIEW_TYPE_LOADING.typeValue else ITEM_TYPES.VIEW_TYPE_VIDEOS.typeValue
        } else {
            ITEM_TYPES.VIEW_TYPE_VIDEOS.typeValue
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewType = youtubeVideosList[position]
        when (viewType.showProgressBar) {
            true -> {
                holder as ProgressHolder
            }
            else -> {
                holder as YoutubeVideoViewHolder
                holder.bindItem( youtubeVideosList[position] )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == 0) {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.youtube_videos_list, parent, false)
            return YoutubeVideoViewHolder(itemView)
        } else {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.loading_list, parent, false)
            return ProgressHolder(itemView)
        }
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ProgressHolder(itemView: View) : ViewHolder(itemView) {
    }

    inner class YoutubeVideoViewHolder( itemView: View ) : ViewHolder(itemView) {

        val ivVideoUrl: ImageView = itemView.ivVideoUrl
        val layoutParent: ConstraintLayout = itemView.parentLayout

        val title: TextView = itemView.tvTitle
        val description: TextView = itemView.tvDescription

        fun bindItem( item: YoutubeVideos) {

            layoutParent.setOnClickListener { view ->
                val intent = Intent(fragmentActivity, YoutubeWeatherActivity::class.java)
                intent.putExtra( "videoId", item.id.videoId )
                fragmentActivity.startActivity(intent)
            }

            title.text = "Title: " + item.snippet.title
            description.text = "Description: " + item.snippet.description

            Glide.with( itemView )
                .load(item.snippet.thumbnails.medium.url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivVideoUrl)
        }
    }

    fun addLoading() {
        isLoaderVisible = true
        val deliverParcel = YoutubeVideos()
        deliverParcel.showProgressBar = true
        youtubeVideosList.add(deliverParcel)
        notifyItemInserted(youtubeVideosList.size - 1)
    }

    fun removeLoading() {
        isLoaderVisible = false
        val position: Int = youtubeVideosList.size - 1
        val item = youtubeVideosList[position]
        if (item != null) {
            youtubeVideosList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return youtubeVideosList.size
    }

    fun updateDevices(items: MutableList<YoutubeVideos>) {
        youtubeVideosList.addAll(items)
        notifyItemRangeInserted(youtubeVideosList.size, items.size)
    }

}