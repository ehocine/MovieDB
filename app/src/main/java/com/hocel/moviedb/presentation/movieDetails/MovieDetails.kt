package com.hocel.moviedb.presentation.movieDetails

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.hocel.moviedb.data.models.trendingMovies.Result
import com.hocel.moviedb.presentation.navigation.Screens
import com.hocel.moviedb.ui.theme.BackgroundColor
import com.hocel.moviedb.ui.theme.CardColor
import com.hocel.moviedb.ui.theme.TextColor
import com.hocel.moviedb.utils.Constants
import com.hocel.moviedb.utils.convertMinutes
import com.hocel.moviedb.utils.uiState.UiState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MovieDetails(
    viewModel: MovieDetailsViewModel = hiltViewModel(),
    navController: NavController,
    movieId: Int
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.getMovieDetailsData(movieId)
        viewModel.getMovieRecommendations(movieId)
    }

    val context = LocalContext.current as ComponentActivity
    val window = context.window
    WindowCompat.setDecorFitsSystemWindows(window, false)
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
    windowInsetsController.let {
        it.hide(WindowInsetsCompat.Type.statusBars())
        it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    val movieDetails by remember { viewModel.movieDetails }

    val movieRecommendations by remember { viewModel.movieRecommendations }

    val movieDetailsUiState by viewModel.movieUiState.collectAsState()
    val movieRecommendationsUiState by viewModel.movieRecommendationUiState.collectAsState()

    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (movieDetailsUiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator()
                }

                is UiState.Success -> {
                    movieDetails?.let {
                        Box {
                            SubcomposeAsyncImage(
                                modifier = Modifier
                                    .height(450.dp)
                                    .fillMaxWidth()
                                    .clip(RectangleShape)
                                    .align(Alignment.TopCenter),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("${Constants.Original_IMAGE_BASE_URL}${it.posterPath}")
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Image"
                            ) {
                                when (painter.state) {
                                    is AsyncImagePainter.State.Loading -> {
                                        Box(
                                            Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }

                                    else -> {
                                        SubcomposeAsyncImageContent(
                                            modifier = Modifier.clip(RectangleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            }

                            Box(
                                Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(horizontal = 16.dp, vertical = 16.dp)
                                    .background(
                                        Color.Black.copy(0.8f),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .padding(6.dp)
                            ) {

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        tint = Color.Yellow,
                                        modifier = Modifier.size(20.dp),
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.padding(3.dp))
                                    Text(
                                        text = String.format("%.1f", it.voteAverage),
                                        style = typography.bodyMedium,
                                        color = Color.White,
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Column(
                            Modifier
                                .padding(bottom = 30.dp)
                                .verticalScroll(state = scrollState)
                        ) {
                            it.title?.let { title ->
                                Text(
                                    text = title,
                                    modifier = Modifier
                                        .padding(16.dp, 0.dp, 0.dp, 0.dp),
                                    style = typography.headlineLarge,
                                    fontWeight = FontWeight.W800,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    color = TextColor,
                                    textAlign = TextAlign.Start
                                )
                            }
                            Text(
                                text = it.releaseDate!!.take(4),
                                color = TextColor,
                                modifier = Modifier
                                    .padding(16.dp, 0.dp, 0.dp, 0.dp),
                            )

                            Spacer(modifier = Modifier.padding(4.dp))

                            Text(
                                text = convertMinutes(it.runtime!!),
                                color = TextColor,
                                modifier = Modifier
                                    .padding(16.dp, 0.dp, 0.dp, 0.dp),
                            )

                            Spacer(modifier = Modifier.padding(4.dp))

                            LazyRow(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp, 0.dp, 0.dp, 0.dp)
                            ) {
                                it.genres?.let { list ->
                                    items(list) { genre ->
                                        genre?.let {
                                            AssistChip(
                                                onClick = {},
                                                enabled = false,
                                                label = {
                                                    Text(
                                                        text = "${genre.name}",
                                                        color = TextColor
                                                    )
                                                },
                                                colors = AssistChipDefaults.assistChipColors(
                                                    labelColor = TextColor
                                                )
                                            )
                                        }
                                        Spacer(modifier = Modifier.padding(4.dp))
                                    }
                                }
                            }
                            Column {
                                Text(
                                    text = "Overview",
                                    modifier = Modifier
                                        .padding(16.dp, 8.dp, 0.dp, 0.dp),
                                    style = typography.titleLarge,
                                    fontWeight = FontWeight.W600,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = TextColor,
                                    textAlign = TextAlign.Start
                                )
                                it.overview?.let { overview ->
                                    Text(
                                        text = overview,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp, 0.dp, 16.dp, 0.dp),
                                        color = TextColor,
                                        fontWeight = FontWeight.W500,
                                        style = typography.bodyLarge,
                                        textAlign = TextAlign.Start
                                    )
                                }

                                Text(
                                    text = "Recommendations",
                                    modifier = Modifier
                                        .padding(16.dp, 8.dp, 0.dp, 0.dp),
                                    style = typography.titleLarge,
                                    fontWeight = FontWeight.W600,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = TextColor,
                                    textAlign = TextAlign.Start
                                )

                                LazyRow(modifier = Modifier.fillMaxWidth()) {
                                    when (movieRecommendationsUiState) {
                                        is UiState.Loading -> {
                                            item {
                                                CircularProgressIndicator()
                                            }
                                        }

                                        is UiState.Success -> {
                                            movieRecommendations?.results?.let { list ->
                                                items(list) { result ->
                                                    ItemMovieCard(
                                                        movie = result,
                                                        onItemClicked = {
                                                            navController.navigate("${Screens.MovieDetails.route}/${result.id}")
                                                        }
                                                    )
                                                }
                                            }
                                        }

                                        else -> {

                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                else -> Unit
            }
        }
        Box(
            Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .background(
                    Color.Black.copy(0.8f),
                    shape = CircleShape
                )
                .padding(3.dp)
        ) {
            IconButton(onClick = {
                navController.navigateUp()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    tint = Color.White,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun ItemMovieCard(movie: Result, onItemClicked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = { onItemClicked() }),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                Modifier
                    .height(150.dp)
                    .width(100.dp)
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(16.dp)),
                    alignment = Alignment.CenterStart,
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("${Constants.IMAGE_BASE_URL}${movie.posterPath}")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Image"
                ) {
                    val state = painter.state
                    if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = TextColor)
                        }
                    } else {
                        SubcomposeAsyncImageContent(
                            modifier = Modifier.clip(RectangleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Box(
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color.Black.copy(0.85f), shape = RoundedCornerShape(10.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = String.format("%.1f", movie.voteAverage),
                        style = typography.bodyMedium,
                        color = Color.White,
                    )
                }
            }
        }
    }
}


