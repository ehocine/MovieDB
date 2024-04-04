package com.hocel.moviedb.data.models.movieDetails

import com.google.gson.annotations.SerializedName

data class MovieVideos(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val movieVideoDetails: List<MovieVideoDetails>
)

data class MovieVideoDetails(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String
)