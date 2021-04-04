package com.vjezba.domain

import java.lang.Exception


/*sealed  class ResultState<out T: Any>
data class Success<out T: Any>(val data: T) : ResultState<T>()
data class Error(val exception: Exception): ResultState<Nothing>()
//object Loading: ResultState<Nothing>()*/


/*
sealed  class ResultState {
    data class Success<out T>(val data: T) : ResultState()
    data class Error(val exception: Exception): ResultState()
    object Loading: ResultState()
}*/


sealed  class ResultState<out T> {
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val exception: Exception): ResultState<Nothing>()
    object Loading: ResultState<Nothing>()
}
