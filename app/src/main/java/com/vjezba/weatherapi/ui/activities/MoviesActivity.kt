package com.vjezba.weatherapi.ui.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.data.networking.ConnectivityUtil
import com.vjezba.weatherapi.R
import com.vjezba.weatherapi.customcontrol.RecyclerViewPaginationListener
import com.vjezba.weatherapi.ui.adapters.MoviesAdapter
import com.vjezba.weatherapi.viewmodels.MoviesViewModel
import com.vjezba.domain.model.MovieResult
import com.vjezba.weatherapi.App
import com.vjezba.weatherapi.network.ConnectivityMonitor
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.android.synthetic.main.activity_movie.*
import javax.inject.Inject


class MoviesActivity : BaseActivity(R.id.no_internet_layout) {

    val moviesViewModel: MoviesViewModel by viewModels()

    private lateinit var moviesAdapter: MoviesAdapter
    val moviesLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

    private var isLastPage = false
    private var loading = false
    private var page: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
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

        moviesViewModel.moviesList.observe(this@MoviesActivity, Observer { news ->
            Log.d(ContentValues.TAG, "Da li ce uci sim uuuuuu: ${news.result.joinToString { "-" }}")
            progressBar.visibility = View.GONE
            if( page > 1 )
                moviesAdapter.removeLoading()
            loading = false
            moviesAdapter.updateDevices(news.result.toMutableList())
        })

        moviesViewModel.getMoviesFromServer(page)
    }

    private fun initializeUi() {

        moviesAdapter = MoviesAdapter( mutableListOf<MovieResult>(),
            { movieId: Long -> setMoviesClickListener( movieId ) }  )

        movies_list.apply {
            layoutManager = moviesLayoutManager
            adapter = moviesAdapter
        }
        movies_list.adapter = moviesAdapter

        /**
         * add scroll listener while user reach in bottom load more will call
         */
        movies_list.addOnScrollListener(object : RecyclerViewPaginationListener(moviesLayoutManager) {

            override fun loadMoreItems() {
                loading = true
                if( connectivityUtil.isConnectedToInternet() )
                    doRestApiCall()
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return loading
            }
        })
    }

    private fun doRestApiCall() {
        moviesAdapter.addLoading()
        page++
        moviesViewModel.getMoviesFromServer(page)
        Log.d(ContentValues.TAG, "Da li ce uci sim uuuuuu pageNumber is: ${page}")
    }

    private fun setMoviesClickListener(movieId: Long) {
        val intent = Intent( this, MoviesDetailsActivity::class.java )
        intent.putExtra("movieId", movieId)
        startActivity(intent)
        finish()
    }

}