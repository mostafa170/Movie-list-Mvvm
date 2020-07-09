package com.kamel.mvvmkotlin.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.kamel.moviemvvm.data.vo.Movie
import com.kamel.mvvmkotlin.data.api.TheMovieDBInterface
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory (private val apiService : TheMovieDBInterface, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, Movie>() {

    val moviesLiveDataSource =  MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService,compositeDisposable)

        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}