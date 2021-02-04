package com.vjezba.weatherapi.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.app.ShareCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vjezba.weatherapi.R
import com.vjezba.weatherapi.viewmodels.MovieDetailsViewModel
import kotlinx.android.synthetic.main.activity_movie_details.*
import java.text.SimpleDateFormat

class MoviesDetailsActivity : BaseActivity(R.id.no_internet_layout) {

    var movieId = 0L

    val movieDetailsViewModel: MovieDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        this.setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        movieId = intent.getLongExtra("movieId", 0L)
    }

    override fun onStart() {
        super.onStart()
        viewLoaded = true

        movieDetailsViewModel.newsDetailsList.observe(this, Observer { movie ->
            tvName.text = movie.originalTitle
            tvHomePage.text = "Web site: " + movie.homepage

            tvHomePage.setOnClickListener {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse( movie.homepage))
                startActivity(browserIntent)
            }

            Glide.with(this)
                .load( "https://image.tmdb.org/t/p/w500/" + movie.backdropPath)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivMovie)

            tvBudget.text = "Budget: " + movie.budget

            val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
            val date: String = simpleDateFormat.format(movie.releaseDate)
            tvReleaseDate.text = "Release date: " + date
        })

        movieDetailsViewModel.getMovieDetails(movieId)

        btnTrailers.setOnClickListener {
            startTrailersActivity()
        }

        btnActors.setOnClickListener {
            startActorsActivity()
        }
    }

    private fun startActorsActivity() {
        val intent = Intent(this, ActorsActivity::class.java)
        intent.putExtra("movieId", movieId)
        startActivity(intent)
    }

    private fun startTrailersActivity() {
        val intent = Intent(this, TrailersActivity::class.java)
        intent.putExtra("movieId", movieId)
        startActivity(intent)
    }

    override fun onNetworkStateUpdated(available: Boolean) {
        super.onNetworkStateUpdated(available)
        if( viewLoaded == true )
            updateConnectivityUi()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_share, menu)
        return true
    }

    @Suppress("DEPRECATION")
    private fun createShareIntent() {
        val shareText = movieDetailsViewModel.newsDetailsList.value.let { data ->
            if (data == null) {
                ""
            } else {
                getString(R.string.share_text_details, data.originalTitle)
            }
        }
        val shareIntent = ShareCompat.IntentBuilder.from(this)
            .setText(shareText)
            .setType("text/plain")
            .createChooserIntent()
            .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        startActivity(shareIntent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent( this, MoviesActivity::class.java )
                startActivity(intent)
                finish()
                true
            }
            R.id.action_share -> {
                createShareIntent()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent( this, MoviesActivity::class.java )
        startActivity(intent)
        finish()
    }

}