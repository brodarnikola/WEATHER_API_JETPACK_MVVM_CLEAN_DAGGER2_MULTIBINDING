package com.vjezba.domain

import java.lang.Exception


/*sealed  class DataState<out T: Any>
data class Success<out T: Any>(val data: T) : DataState<T>()
data class Error(val exception: Exception): DataState<Nothing>()
//object Loading: DataState<Nothing>()*/


/*
sealed  class DataState {
    data class Success<out T>(val data: T) : DataState()
    data class Error(val exception: Exception): DataState()
    object Loading: DataState()
}*/


sealed  class DataState<out T> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val exception: Exception): DataState<Nothing>()
    object Loading: DataState<Nothing>()
}
