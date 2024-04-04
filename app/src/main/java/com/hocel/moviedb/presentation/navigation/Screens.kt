package com.hocel.moviedb.presentation.navigation

sealed class Screens(val route: String) {
    data object MoviesListScreen : Screens(route = "movies_list_screen")
    data object MovieDetails : Screens(route = "movie_details")
}