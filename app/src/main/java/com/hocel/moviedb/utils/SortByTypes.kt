package com.hocel.moviedb.utils

enum class SortByTypes(val value: String, val code: String) {
    PopularityDesc(value = "Popularity descending", code = "popularity.desc"),
    PopularityAsc(value = "Popularity ascending", code = "popularity.asc"),
    TitleDesc(value = "Title descending", code = "original_title.desc"),
    TitleAsc(value = "Title ascending", code = "original_title.asc"),
    ReleaseDateDesc(value = "Release date descending", code = "primary_release_date.desc"),
    ReleaseDateAsc(value = "Release date ascending", code = "primary_release_date.asc"),
    RatingDesc(value = "Rating descending", code = "vote_average.desc"),
    RatingAsc(value = "Rating ascending", code = "vote_average.asc"),
}

