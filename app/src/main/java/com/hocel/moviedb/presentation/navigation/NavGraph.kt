package com.hocel.moviedb.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hocel.moviedb.presentation.movieDetails.MovieDetailsScreen
import com.hocel.moviedb.presentation.moviesList.MoviesListScreen


@Composable
fun NavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screens.MoviesListScreen.route,
        route = "root"
    ) {

        composable(
            route = Screens.MoviesListScreen.route,
        ) {
            MoviesListScreen(
                navController = navController
            )
        }


        composable(
            route = "${Screens.MovieDetails.route}/{movieId}",
            arguments = listOf(
                navArgument(name = "movieId") { type = NavType.IntType }
            )
        ) { from ->
            MovieDetailsScreen(
                navController = navController,
                movieId = from.arguments?.getInt("movieId") ?: 0
            )
        }

    }
}