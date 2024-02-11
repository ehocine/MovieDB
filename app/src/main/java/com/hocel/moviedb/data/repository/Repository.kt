package com.hocel.moviedb.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.hocel.moviedb.data.api.MoviesService
import com.hocel.moviedb.data.models.genres.Genres
import com.hocel.moviedb.data.models.movieDetails.MovieDetailsResponse
import com.hocel.moviedb.data.models.trendingMovies.MoviesResponse
import com.hocel.moviedb.presentation.trendingMovies.SearchMoviesPagingSource
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

    fun searchMovies(query: String) = Pager(
        config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 1
        ),
        pagingSourceFactory = {
            SearchMoviesPagingSource(api, query)
        }
    ).flow

    suspend fun getMovieRecommendations(movieId: Int): Response<MoviesResponse> {
        return api.movieRecommendations(movieId)
    }

    suspend fun getListOfGenres(): Genres = api.getListOfGenres()
}