package com.vjezba.weatherapi.ui.activities

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.domain.model.TrailerResult
import com.vjezba.weatherapi.R
import com.vjezba.weatherapi.ui.adapters.TrailersAdapter
import com.vjezba.weatherapi.viewmodels.TrailersViewModel
import kotlinx.android.synthetic.main.activity_trailers.*


class TrailersActivity : BaseActivity(R.id.no_internet_layout) {

    var movieId = 0L

    val trailerViewModel: TrailersViewModel by viewModels()

    private lateinit var trailerAdapter: TrailersAdapter
    val trailerLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trailers)

        this.setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        movieId = intent.getLongExtra("movieId", 0L)
    }

    override fun onNetworkStateUpdated(available: Boolean) {
        super.onNetworkStateUpdated(available)
        if( viewLoaded == true )
            updateConnectivityUi()
    }

    override fun onStart() {
        super.onStart()
        viewLoaded = true

        initializeUi()

        trailerViewModel.trailerList.observe(this@TrailersActivity, Observer { news ->
            Log.d(ContentValues.TAG, "Da li ce uci sim uuuuuu uu TRAILERSS: ${news.results.joinToString { "-" }}")
            progressBar.visibility = View.GONE
            trailerAdapter.update(news.results.toMutableList())
        })

        trailerViewModel.getTrailers(movieId)
    }

    private fun initializeUi() {
        trailerAdapter = TrailersAdapter( mutableListOf<TrailerResult>() )

        trailer_list.apply {
            layoutManager = trailerLayoutManager
            adapter = trailerAdapter
        }
        trailer_list.adapter = trailerAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}