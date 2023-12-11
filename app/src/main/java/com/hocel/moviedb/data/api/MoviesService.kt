package com.hocel.moviedb.data.api

import com.hocel.moviedb.data.models.movieDetails.MovieDetailsResponse
import com.hocel.moviedb.data.models.trendingMovies.TrendingMoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {

    @GET("3/discover/movie")
    suspend fun getTrendingMovies(
        @Query("page") page: Int
    ): TrendingMoviesResponse


    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int
    ): Response<MovieDetailsResponse>

}