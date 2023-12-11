package com.hocel.moviedb.presentation.navigation

sealed class Screens(val route: String) {
    data object TrendingMoviesScreen : Screens(route = "trending_movies_screen")
    data object MovieDetails : Screens(route = "movie_details")
}