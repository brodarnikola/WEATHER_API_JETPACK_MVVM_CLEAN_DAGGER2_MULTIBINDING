/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vjezba.weatherapi.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vjezba.data.di_dagger2.youtube.YoutubeNetwork
import com.vjezba.domain.model.youtube.YoutubeVideosMain
import com.vjezba.domain.repository.YoutubeRepository
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class YoutubeViewModel @ViewModelInject constructor(
    @YoutubeNetwork private val repository: YoutubeRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _youtubeMutableLiveData = MutableLiveData<YoutubeVideosMain>().apply {
        value = YoutubeVideosMain( "", listOf())
    }

    val youtubeListLiveData: LiveData<YoutubeVideosMain> = _youtubeMutableLiveData

    fun getYoutubeVideos(youtubeKeyWord: String) {
        val part = "Snippet"
        val maxResults = 50
        val type = "video"
        repository.getYoutubeVideosFromKeyWord(part, maxResults, youtubeKeyWord, type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .onErrorResumeNext { throwable: Throwable ->
                return@onErrorResumeNext ObservableSource {
                    _youtubeMutableLiveData.value?.let {
                        _youtubeMutableLiveData.value = YoutubeVideosMain()
                    }
                    Log.d("onErrorResumeNext", "on exception resume next, update ui" + throwable.localizedMessage)
                }
            }
            .subscribe(object : Observer<YoutubeVideosMain> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(response: YoutubeVideosMain) {
                    _youtubeMutableLiveData.value?.let {
                        _youtubeMutableLiveData.value = response
                    }
                }

                override fun onError(e: Throwable) {
                    Log.d(
                        ContentValues.TAG,
                        "onError received: " + e.message
                    )
                }

                override fun onComplete() {

                }
            })
    }

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()
    }

}

