package com.vjezba.weatherapi.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vjezba.weatherapi.R
import com.vjezba.domain.model.WeatherData
import kotlinx.android.synthetic.main.weather_list.view.*

class WeatherAdapter(
    var weatherResultList: MutableList<WeatherData>,
    val weatherResultClickListener: (Long) -> Unit
) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    private var isLoaderVisible = false

    enum class ITEM_TYPES(val typeValue: Int) {
        VIEW_TYPE_NORMAL(0),
        VIEW_TYPE_LOADING(1);

        companion object {
            fun from(findViewByIdValue: Int): ITEM_TYPES =
                values().first { it.typeValue == findViewByIdValue }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            if (position == weatherResultList.size - 1) ITEM_TYPES.VIEW_TYPE_LOADING.typeValue else ITEM_TYPES.VIEW_TYPE_NORMAL.typeValue
        } else {
            ITEM_TYPES.VIEW_TYPE_NORMAL.typeValue
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder as WeatherViewHolder
        holder.bindItem( holder, weatherResultList[position] )
//        val viewType = weatherResultList[position]
//        when (viewType.showProgressBar) {
//            true -> {
//                holder as ProgressHolder
//            }
//            else -> {
//
//                holder as WeatherViewHolder
//                holder.bindItem( holder, weatherResultList[position] )
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if (viewType == 0) {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.weather_list, parent, false)
            return WeatherViewHolder(itemView)
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

    inner class WeatherViewHolder(itemView: View) : ViewHolder(itemView) {
        val layoutParent: ConstraintLayout = itemView.parentLayout

        val tvTemp: TextView = itemView.tvTemp
        val tvMax: TextView = itemView.tvMax
        val tvFeelsLike: TextView = itemView.tvFeelsLike
        val tvWind: TextView = itemView.tvWind
        val tvDescription: TextView = itemView.tvDescription
        val tvDate: TextView = itemView.tvDate

        fun bindItem(holder: ViewHolder, item: WeatherData) {

//            Glide.with(holder.itemView)
//                .load("https://image.tmdb.org/t/p/w500/")
//                .placeholder(R.drawable.placeholder)
//                .error(R.drawable.placeholder)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(photo)

            tvTemp.text = "Temp: " + item.main.temp
            tvMax.text = "Temp max: " + item.main.tempMax
            tvFeelsLike.text = "Feels like: " + item.main.feelsLike
            tvWind.text = "Wind Speed: " + item.wind.speed
            if( item.weather.isNotEmpty() ) {
                tvDescription.visibility = View.VISIBLE
                tvDescription.text = "Description: " + item.weather[0].description
            }
            else
                tvDescription.visibility = View.GONE
            tvDate.text = "Date and time: " + item.dateAndTime

            layoutParent.setOnClickListener {
                //item.id?.let { it -> weatherResultClickListener(it) }
            }
        }
    }

    override fun getItemCount(): Int {
        return weatherResultList.size
    }


    fun addLoading() {
//        isLoaderVisible = true
//        val deliverParcel = WeatherData()
//        deliverParcel.showProgressBar = true
//        weatherResultList.add(deliverParcel)
//        notifyItemInserted(weatherResultList.size - 1)
    }

    fun removeLoading() {
//        isLoaderVisible = false
//        val position: Int = weatherResultList.size - 1
//        val item = weatherResultList[position]
//        weatherResultList.removeAt(position)
//        notifyItemRemoved(position)
    }

    fun updateDevices(updatedDevices: MutableList<WeatherData>) {
        weatherResultList.addAll(updatedDevices)
        notifyItemRangeInserted(weatherResultList.size, updatedDevices.size)
    }


}