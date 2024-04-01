package com.hocel.moviedb.data.models.movieDetails


import com.google.gson.annotations.SerializedName
import com.hocel.moviedb.data.models.genres.Genre

data class MovieDetails(
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("genres")
    val genres: List<Genre?>?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("original_language")
    val originalLanguage: String?,
    @SerializedName("original_title")
    val originalTitle: String?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("popularity")
    val popularity: Double?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("runtime")
    val runtime: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
)