package com.hocel.moviedb.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.hocel.moviedb.data.api.MoviesService
import com.hocel.moviedb.data.models.movieDetails.MovieDetailsResponse
import com.hocel.moviedb.presentation.trendingMovies.TrendingMoviesPagingSource
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: MoviesService
) {

    fun getTrendingMoviesPaged() = Pager(
        config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 1
        ),
        pagingSourceFactory = {
            TrendingMoviesPagingSource(api)
        }
    ).flow

    suspend fun getMovieDetails(movieId: Int): Response<MovieDetailsResponse> {
        return api.getMovieDetails(movieId)
    }
}