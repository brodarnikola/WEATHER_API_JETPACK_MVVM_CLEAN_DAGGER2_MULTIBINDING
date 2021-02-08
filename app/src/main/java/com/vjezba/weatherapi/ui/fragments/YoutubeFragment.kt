package com.vjezba.weatherapi.ui.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.weatherapi.customcontrol.RecyclerViewPaginationListener
import com.vjezba.weatherapi.databinding.FragmentYoutubeBinding
import com.vjezba.weatherapi.ui.adapters.YoutubeVideosAdapter
import com.vjezba.weatherapi.viewmodels.YoutubeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.fragment_youtube.*

@AndroidEntryPoint
class YoutubeFragment : Fragment() {

    val youtubeViewModel: YoutubeViewModel by viewModels()

    private lateinit var youtubeVideosAdapter: YoutubeVideosAdapter
    var youtubeVideoLayoutManager: LinearLayoutManager? = null

    lateinit var binding: FragmentYoutubeBinding

    private var isLastPage = false
    private var loading = false
    private var page: Int = 1

    var lastInsertedKeyWordText = ""
    var nextPageToken = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = FragmentYoutubeBinding.inflate(inflater, container, false)
        context ?: return binding.root

        activity?.tvToolbarTitle?.text = "YOUTUBE VIDEOS"

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initializeUi()

        setupClickListeners()

        setupObservers()
    }

    private fun setupObservers() {

        youtubeViewModel.youtubeListLiveData.observe(this@YoutubeFragment, Observer { items ->
            Log.d(ContentValues.TAG, "Data is: ${items.items.joinToString { "-" }}")

            if( page > 1 && youtubeVideosAdapter.youtubeVideosList.isNotEmpty())
                youtubeVideosAdapter.removeLoading()
            loading = false

            nextPageToken = items.nextPageToken
            Log.d(ContentValues.TAG, "Next page token is: ${nextPageToken}")

            binding.btnShowYoutubeVideos.isEnabled = true
            binding.btnShowYoutubeVideos.alpha = 1.0f

            youtubeVideosAdapter.updateDevices(items.items.toMutableList())
        })
    }

    private fun setupClickListeners() {

        binding.btnShowYoutubeVideos.setOnClickListener {
            it.isEnabled = false
            it.alpha = 0.4f

            nextPageToken = ""
            page = 1
            lastInsertedKeyWordText = etYoutubeKeyWord.text.toString()

            youtubeVideosAdapter.youtubeVideosList.clear()
            youtubeVideosAdapter.notifyDataSetChanged()

            youtubeViewModel.getYoutubeVideos(etYoutubeKeyWord.text.toString(), "")
        }
    }

    private fun initializeUi() {

        youtubeVideoLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        youtubeVideosAdapter = YoutubeVideosAdapter( mutableListOf(), requireActivity() )

        youtube_list.apply {
            layoutManager = youtubeVideoLayoutManager
            adapter = youtubeVideosAdapter
        }

        /**
         * add scroll listener while user reach in bottom load more will call
         */
        youtube_list.addOnScrollListener(object : RecyclerViewPaginationListener(youtubeVideoLayoutManager!!) {

            override fun loadMoreItems() {
                loading = true
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
        youtubeVideosAdapter.addLoading()
        page++
        youtubeViewModel.getYoutubeVideos(lastInsertedKeyWordText, nextPageToken)

        Log.d(ContentValues.TAG, "Da li ce uci sim uuuuuu pageNumber is: ${page}")
    }

}