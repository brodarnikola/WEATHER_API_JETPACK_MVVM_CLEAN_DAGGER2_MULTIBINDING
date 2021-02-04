package com.vjezba.weatherapi.ui.activities

import android.app.Activity
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.weatherapi.R
import com.vjezba.weatherapi.di.ViewModelFactory
import com.vjezba.weatherapi.di.injectViewModel
import com.vjezba.weatherapi.ui.adapters.ActorsAdapter
import com.vjezba.weatherapi.viewmodels.ActorsViewModel
import com.vjezba.domain.model.ActorsResult
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import kotlinx.android.synthetic.main.activity_actors.*
import javax.inject.Inject


class ActorsActivity : BaseActivity(R.id.no_internet_layout), HasActivityInjector {

    var movieId = 0L

    @Inject
    lateinit var dispatchingAndroidActivityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = dispatchingAndroidActivityInjector


    @Inject lateinit var viewModelFactory: ViewModelFactory
    lateinit var actorsViewModel: ActorsViewModel

    private lateinit var actorsAdapter: ActorsAdapter
    val trailerLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actors)

        actorsViewModel = injectViewModel(viewModelFactory)

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