package com.hocel.moviedb.data.models.movieDetails

import com.google.gson.annotations.SerializedName

data class MovieReviews(
    val id: Int,
    val page: Int,
    @SerializedName("results")
    val reviews: List<Review>,
    val total_pages: Int,
    val total_results: Int
)

data class AuthorDetails(
    val avatar_path: String,
    val name: String,
    val rating: Int,
    val username: String
)

data class Review(
    val author: String,
    val author_details: AuthorDetails,
    val content: String,
    val created_at: String,
    val id: String,
    val updated_at: String,
    val url: String
)