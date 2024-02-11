package com.hocel.moviedb.data.api

import com.hocel.moviedb.data.models.genres.Genres
import com.hocel.moviedb.data.models.movieDetails.MovieDetailsResponse
import com.hocel.moviedb.data.models.trendingMovies.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {

    @GET("3/discover/movie")
    suspend fun getTrendingMovies(
        @Query("page") page: Int
    ): MoviesResponse


    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int
    ): Response<MovieDetailsResponse>

    @GET("3/search/movie")
    suspend fun searchForMovies(
        @Query("query") query: String,
        @Query("page") page: Int
    ): MoviesResponse

    @GET("3/movie/{movie_id}/similar")
    suspend fun movieRecommendations(
        @Path("movie_id") movieId: Int
    ): Response<MoviesResponse>

    @GET("3/genre/movie/list")
    suspend fun getListOfGenres(): Genres

}