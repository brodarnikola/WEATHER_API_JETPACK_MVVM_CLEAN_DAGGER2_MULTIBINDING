package com.vjezba.weatherapi.di

import androidx.lifecycle.ViewModel
import com.vjezba.weatherapi.viewmodels.*

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    abstract fun bindMoviesActivityiViewModel(viewModel: MoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    abstract fun bindMOvieDetailsActivityViewModel(viewModel: MovieDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TrailersViewModel::class)
    abstract fun bindTrailersActivityViewModel(viewModel: TrailersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ActorsViewModel::class)
    abstract fun bindActorsActivityViewModel(viewModel: ActorsViewModel): ViewModel

}
