package com.vjezba.weatherapi.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vjezba.domain.model.youtube.YoutubeVideos
import com.vjezba.weatherapi.R
import com.vjezba.weatherapi.databinding.YoutubeVideosListBinding
import com.vjezba.weatherapi.ui.activities.YoutubeWeatherActivity

class YoutubeVideosAdapter(
    var youtubeVideosList: MutableList<YoutubeVideos>,
    var mFragmentActivity: FragmentActivity
) : RecyclerView.Adapter<YoutubeVideosAdapter.YoutubeVideoViewHolder>() {

    var fragmentActivity: FragmentActivity = mFragmentActivity

    override fun onBindViewHolder(holder: YoutubeVideoViewHolder, position: Int) {
        holder.bindItem( youtubeVideosList[position] )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoutubeVideoViewHolder {
        return YoutubeVideoViewHolder(
            YoutubeVideosListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class YoutubeVideoViewHolder( private val binding: YoutubeVideosListBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.parentLayout.setOnClickListener { view ->
                val intent = Intent(fragmentActivity, YoutubeWeatherActivity::class.java)
                intent.putExtra( "videoId", binding.items?.id?.videoId )
                fragmentActivity.startActivity(intent)
            }
        }

        fun bindItem( item: YoutubeVideos) {

            binding.apply {
                val finalTitle = "Title: " + item.snippet.title
                item.snippet.title= finalTitle
                items = item
                executePendingBindings()
                tvDescription.text = "Description: " + item.snippet.description

            Glide.with( itemView )
                .load(item.snippet.thumbnails.medium.url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivVideoUrl)
            }

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