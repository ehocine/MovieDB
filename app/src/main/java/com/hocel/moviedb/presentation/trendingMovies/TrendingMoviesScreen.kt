package com.hocel.moviedb.presentation.trendingMovies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.hocel.moviedb.data.models.trendingMovies.Result
import com.hocel.moviedb.presentation.components.FailedView
import com.hocel.moviedb.presentation.navigation.Screens
import com.hocel.moviedb.ui.theme.BackgroundColor
import com.hocel.moviedb.ui.theme.TextColor
import com.hocel.moviedb.utils.showStatusBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingMoviesScreen(
    viewModel: TrendingMoviesViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        showStatusBar(context)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    val trendingMoviesListFlow = remember(viewModel.pagedTrendingMovies, lifecycleOwner) {
        viewModel.pagedTrendingMovies.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }
    val moviesListLazy = trendingMoviesListFlow.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Trending movies",
                        color = TextColor,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .background(BackgroundColor)
                .fillMaxSize()
        ) {
            TrendingMoviesList(moviesListLazy, navController)
        }
    }
}

@Composable
private fun TrendingMoviesList(
    moviesListLazy: LazyPagingItems<Result>?,
    navController: NavController
) {
    LazyColumn(
        contentPadding = PaddingValues(
            top = 10.dp
        )
    ) {
        moviesListLazy?.let { it ->
            items(count = it.itemCount) { index ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    index.let {
                        val item = moviesListLazy[it]!!

                        ItemMovieCard(
                            movie = item,
                            onItemClicked = {
                                navController.navigate("${Screens.MovieDetails.route}/${item.id}")
                            }
                        )
                    }
                }
            }
        }
        when (moviesListLazy?.loadState?.refresh) {

            is LoadState.Error -> {
                item {
                    FailedView(
                        modifier = Modifier.fillParentMaxSize(),
                        retryable = true,
                        onRetry = {
                            moviesListLazy::retry.invoke()
                        }
                    )
                }
            }

            is LoadState.Loading -> {
                item {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(top = 12.dp)
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            is LoadState.NotLoading -> Unit
            else -> {}
        }
        when (moviesListLazy?.loadState?.append) {
            is LoadState.Error -> {
                item {
                    FailedView(
                        modifier = Modifier.fillParentMaxSize(),
                        retryable = true,
                        onRetry = {
                            moviesListLazy::retry.invoke()
                        }
                    )
                }
            }

            is LoadState.Loading -> {
                item {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 12.dp)
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            is LoadState.NotLoading -> {
                if (moviesListLazy.loadState.append.endOfPaginationReached && moviesListLazy.itemCount == 0) {
                    item {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillParentMaxSize()
                        ) {
                            Text(text = "No result")
                        }
                    }
                }
            }

            else -> {}
        }
    }
}