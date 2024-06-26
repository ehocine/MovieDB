package com.hocel.moviedb.data.api

import com.hocel.moviedb.data.models.genres.Genres
import com.hocel.moviedb.data.models.movieDetails.MovieDetails
import com.hocel.moviedb.data.models.movieDetails.MovieReviews
import com.hocel.moviedb.data.models.movieDetails.MovieVideos
import com.hocel.moviedb.data.models.moviesList.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {

    @GET("3/discover/movie")
    suspend fun getTrendingMovies(
        @Query("page") page: Int,
        @Query("vote_average.gte") rating: Float,
        @Query("with_genres") genre: String,
        @Query("sort_by") sortBy: String
    ): MoviesResponse


    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int
    ): Response<MovieDetails>

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


    @GET("3/movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int
    ): Response<MovieVideos>

    @GET("3/movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Int
    ): Response<MovieReviews>

}