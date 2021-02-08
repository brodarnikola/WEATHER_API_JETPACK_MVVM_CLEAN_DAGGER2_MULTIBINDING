

package com.vjezba.data.repository

import com.vjezba.data.API_KEY_FOR_YOUTUBE_VIDEOS
import com.vjezba.data.database.mapper.DbMapper
import com.vjezba.data.networking.youtube.YoutubeRepositoryApi
import com.vjezba.domain.model.youtube.YoutubeVideosMain
import com.vjezba.domain.repository.YoutubeRepository
import io.reactivex.Flowable

/**
 * RepositoryResponseApi module for handling data operations.
 */

class YoutubeRepositoryImpl constructor(
    private val service: YoutubeRepositoryApi,
    private val dbMapper: DbMapper?
) : YoutubeRepository {

    override fun getYoutubeVideosFromKeyWord(part: String, maxResults: Int, youtubeKeyWord: String, type: String, nextPage: String): Flowable<YoutubeVideosMain> {

        val result =  if ( nextPage != "" ) {
            service.getYoutubeVideosNextPage(
                part,
                maxResults,
                youtubeKeyWord,
                type,
                nextPage,
                API_KEY_FOR_YOUTUBE_VIDEOS
            )
        }
        else {
            service.getYoutubeVideos(
                part,
                maxResults,
                youtubeKeyWord,
                type,
                API_KEY_FOR_YOUTUBE_VIDEOS
            )
        }

        val correctResults = result.map { dbMapper?.mapApiYoutubeVideosToDomainYoutube(it)!! }
        return correctResults
    }

}
