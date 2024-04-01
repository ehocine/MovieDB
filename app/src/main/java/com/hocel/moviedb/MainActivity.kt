package com.hocel.moviedb

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hocel.moviedb.presentation.navigation.NavGraph
import com.hocel.moviedb.presentation.trendingMovies.TrendingMoviesScreen
import com.hocel.moviedb.ui.theme.BackgroundColor
import com.hocel.moviedb.ui.theme.MovieDBTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
        super.onCreate(savedInstanceState)
        //this.actionBar?.hide()
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
