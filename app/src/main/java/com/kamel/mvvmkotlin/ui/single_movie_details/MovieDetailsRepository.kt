package com.kamel.mvvmkotlin.ui.single_movie_details

import androidx.lifecycle.LiveData
import com.kamel.moviemvvm.data.vo.MovieDetails
import com.kamel.mvvmkotlin.data.api.TheMovieDBInterface
import com.kamel.mvvmkotlin.data.repository.MovieDetailsNetworkDataSource
import com.oxcoding.moviemvvm.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository (private val apiService : TheMovieDBInterface) {

    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails (compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails> {

        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService,compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieResponse

    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }



}