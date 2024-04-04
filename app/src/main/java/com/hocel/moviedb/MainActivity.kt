package com.hocel.moviedb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hocel.moviedb.presentation.navigation.NavGraph
import com.hocel.moviedb.ui.theme.BackgroundColor
import com.hocel.moviedb.ui.theme.MovieDBTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieDBTheme {
                navController = rememberNavController()
                val systemUiController = rememberSystemUiController()
                val systemUIColor = BackgroundColor
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = systemUIColor
                    )
                }
                NavGraph(navController = navController)
            }
        }
    }
}
