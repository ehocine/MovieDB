package com.hocel.moviedb.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hocel.moviedb.presentation.movieDetails.MovieDetails
import com.hocel.moviedb.presentation.trendingMovies.TrendingMoviesScreen


@Composable
fun NavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screens.TrendingMoviesScreen.route,
        route = "root"
    ) {

        composable(
            route = Screens.TrendingMoviesScreen.route
        ) {
            TrendingMoviesScreen(navController = navController)
        }


        composable(
            route = "${Screens.MovieDetails.route}/{movieId}",
            arguments = listOf(
                navArgument(name = "movieId") { type = NavType.IntType }
            )
        ) { from ->
            MovieDetails(
                navController = navController,
                movieId = from.arguments?.getInt("movieId") ?: 0
            )
        }

    }
}