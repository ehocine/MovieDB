package com.hocel.moviedb.presentation.trendingMovies

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
    var showSearchBar by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var searchBarActive by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val listOfGenres by viewModel.listOfGenres

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Trending movies",
                                color = TextColor,
                                fontWeight = FontWeight.Bold
                            )
                            if (!showSearchBar) {
                                Icon(
                                    modifier = Modifier.clickable {
                                        showSearchBar = !showSearchBar
                                    },
                                    imageVector = Icons.Default.Search,
                                    tint = TextColor,
                                    contentDescription = "Search icon"
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor)
            )
        }
    ) { paddingValues ->
        if (showBottomSheet) {
            ModalBottomSheet(
                containerColor = BackgroundColor,
                onDismissRequest = { showBottomSheet = false }) {
                FilterSheetContent(listOfGenres)
            }
        }
        Box(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .background(BackgroundColor)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.padding(6.dp)) {
                SearchBar(
                    showSearchBar = showSearchBar,
                    searchBarActive = searchBarActive,
                    searchText = searchText,
                    onSearch = {

                    },
                    onQueryChange = {
                        searchText = it
                        viewModel.searchMovies(it)
                    },
                    onActiveChange = {
                        showSearchBar = it
                        searchBarActive = it
                    },
                    onCloseClicked = {
                        if (searchText.isNotBlank()) {
                            searchText = ""
                        } else {
                            showSearchBar = false
                            searchBarActive = false
                            viewModel.getTrendingMoviesPaged()
                        }
                    },
                    onFilterClicked = {
                        showBottomSheet = true
                    }
                )
                TrendingMoviesList(moviesListLazy = moviesListLazy, navController = navController)
            }
        }
    }
}

@Composable
private fun TrendingMoviesList(
    modifier: Modifier = Modifier,
    moviesListLazy: LazyPagingItems<Result>?,
    navController: NavController
) {
    LazyColumn(
        modifier = modifier,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    showSearchBar: Boolean,
    searchBarActive: Boolean,
    searchText: String,
    onSearch: (String) -> Unit,
    onQueryChange: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,
    onCloseClicked: () -> Unit,
    onFilterClicked: () -> Unit
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = showSearchBar,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
    ) {
        DockedSearchBar(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            query = searchText,
            onQueryChange = {
                onQueryChange(it)
            },
            onSearch = {
                onSearch(it)
            },
            active = false,
            onActiveChange = {
                onActiveChange(it)
            },
            placeholder = {
                Text(text = "Search", color = TextColor)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = TextColor
                )
            },
            trailingIcon = {
                Row {
                    Icon(
                        modifier = Modifier.clickable {
                            onCloseClicked()
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = TextColor
                    )
                    Icon(
                        modifier = Modifier.clickable {
                            onFilterClicked()
                        },
                        imageVector = Icons.Default.Menu,
                        contentDescription = null,
                        tint = TextColor
                    )
                }
            }
        ) {

        }
    }
}