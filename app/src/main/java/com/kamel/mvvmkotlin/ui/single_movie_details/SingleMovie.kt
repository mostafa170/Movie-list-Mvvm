package com.kamel.mvvmkotlin.ui.single_movie_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.kamel.moviemvvm.data.vo.MovieDetails
import com.kamel.mvvmkotlin.R
import com.kamel.mvvmkotlin.data.api.POSTER_BASE_URL
import com.kamel.mvvmkotlin.data.api.TheMovieDBClient
import com.kamel.mvvmkotlin.data.api.TheMovieDBInterface
import com.kamel.mvvmkotlin.databinding.ActivitySingleMovieBinding
import com.oxcoding.moviemvvm.data.repository.NetworkState
import kotlinx.android.synthetic.main.activity_single_movie.*
import java.text.NumberFormat
import java.util.*

class SingleMovie : AppCompatActivity() {

    lateinit var singleMovieBinding: ActivitySingleMovieBinding
    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        singleMovieBinding = DataBindingUtil.setContentView(this, R.layout.activity_single_movie)

        val movieId: Int = intent.getIntExtra("id",1)

        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            singleMovieBinding.progressBar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            singleMovieBinding.txtError.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE

        })
    }
    fun bindUI( it: MovieDetails){
        singleMovieBinding.movieTitle.text = it.title
        singleMovieBinding.movieTagline.text = it.tagline
        singleMovieBinding.movieReleaseDate.text = it.releaseDate
        singleMovieBinding.movieRating.text = it.rating.toString()
        singleMovieBinding.movieRuntime.text = it.runtime.toString() + " minutes"
        singleMovieBinding.movieOverview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        singleMovieBinding.movieBudget.text = formatCurrency.format(it.budget)
        singleMovieBinding.movieRevenue.text = formatCurrency.format(it.revenue)

        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(singleMovieBinding.ivMoviePoster);


    }


    private fun getViewModel(movieId:Int): SingleMovieViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieRepository,movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}
