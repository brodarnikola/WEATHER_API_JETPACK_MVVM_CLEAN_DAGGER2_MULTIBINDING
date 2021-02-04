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
import com.vjezba.domain.model.ActorsResult
import com.vjezba.weatherapi.R
import com.vjezba.weatherapi.ui.adapters.ActorsAdapter
import com.vjezba.weatherapi.viewmodels.ActorsViewModel
import kotlinx.android.synthetic.main.activity_actors.*


class ActorsActivity : BaseActivity(R.id.no_internet_layout) {

    var movieId = 0L

    val actorsViewModel: ActorsViewModel by viewModels()

    private lateinit var actorsAdapter: ActorsAdapter
    val trailerLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actors)

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

        actorsViewModel.actorsList.observe(this@ActorsActivity, Observer { news ->
            Log.d(ContentValues.TAG, "Da li ce uci sim uuuuuu uu ACTORS: ${news.cast.joinToString { "-" }}")
            progressBar.visibility = View.GONE
            actorsAdapter.update(news.cast.toMutableList())
        })

        actorsViewModel.getActors(movieId)
    }

    private fun initializeUi() {
        actorsAdapter = ActorsAdapter( mutableListOf<ActorsResult>() )

        actors_list.apply {
            layoutManager = trailerLayoutManager
            adapter = actorsAdapter
        }
        actors_list.adapter = actorsAdapter
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