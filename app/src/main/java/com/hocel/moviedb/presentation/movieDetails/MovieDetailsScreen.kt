package com.hocel.moviedb.presentation.movieDetails

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.hocel.moviedb.data.models.movieDetails.MovieDetails
import com.hocel.moviedb.data.models.movieDetails.MovieReviews
import com.hocel.moviedb.data.models.movieDetails.MovieVideoDetails
import com.hocel.moviedb.data.models.movieDetails.Review
import com.hocel.moviedb.data.models.moviesList.Movie
import com.hocel.moviedb.data.models.moviesList.MoviesResponse
import com.hocel.moviedb.presentation.components.SmallMovieCard
import com.hocel.moviedb.presentation.navigation.Screens
import com.hocel.moviedb.ui.theme.BackgroundColor
import com.hocel.moviedb.ui.theme.CardColor
import com.hocel.moviedb.ui.theme.TextColor
import com.hocel.moviedb.utils.Constants
import com.hocel.moviedb.utils.convertMinutes
import com.hocel.moviedb.utils.uiState.UiState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel = hiltViewModel(), navController: NavController, movieId: Int
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.getMovieDetailsData(movieId)
        viewModel.getMovieRecommendations(movieId)
        viewModel.getMovieVideos(movieId)
        viewModel.getMovieReviews(movieId)
    }

    val context = LocalContext.current as ComponentActivity

    val movieDetailsUiState by viewModel.movieDetailsUiState
    val movieRecommendationsUiState by viewModel.movieRecommendationUiState
    val movieVideosUiState by viewModel.movieVideosUiState
    val movieReviewsUiState by viewModel.movieReviewsUiState

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
                    ((movieDetailsUiState as UiState.Success).data as? MovieDetails)?.let {
                        MoviePoster(movieDetails = it)
                        Spacer(modifier = Modifier.height(24.dp))
                        MovieContent(
                            movieDetails = it,
                            movieRecommendationsUiState = movieRecommendationsUiState,
                            movieVideosUiState = movieVideosUiState,
                            movieReviewsUiState = movieReviewsUiState
                        ) { result ->
                            navController.navigate("${Screens.MovieDetails.route}/${result.id}")
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
                    Color.Black.copy(0.8f), shape = CircleShape
                )
                .padding(3.dp)
        ) {
            IconButton(onClick = {
                navController.navigateUp()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = Color.White,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun MovieContent(
    modifier: Modifier = Modifier,
    movieDetails: MovieDetails,
    movieRecommendationsUiState: UiState,
    movieVideosUiState: UiState,
    movieReviewsUiState: UiState,
    onItemClicked: (movie: Movie) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .padding(bottom = 30.dp)
            .verticalScroll(state = scrollState)
    ) {
        movieDetails.title?.let { title ->
            Text(
                text = title,
                modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
                style = typography.headlineLarge,
                fontWeight = FontWeight.W800,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = TextColor,
                textAlign = TextAlign.Start
            )
        }
        Text(
            text = movieDetails.releaseDate!!.take(4),
            color = TextColor,
            modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Text(
            text = convertMinutes(movieDetails.runtime!!),
            color = TextColor,
            modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
        )

        Spacer(modifier = Modifier.padding(4.dp))

        LazyRow(
            Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp, 0.dp, 0.dp)
        ) {
            movieDetails.genres?.let { list ->
                items(list) { genre ->
                    genre?.let {
                        AssistChip(onClick = {}, enabled = false, label = {
                            Text(
                                text = genre.name, color = TextColor
                            )
                        }, colors = AssistChipDefaults.assistChipColors(
                            labelColor = TextColor
                        )
                        )
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                }
            }
        }
        Column {
            when (movieVideosUiState) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Success -> {
                    Spacer(modifier = Modifier.padding(16.dp))
                    (movieVideosUiState.data as? MovieVideoDetails)?.let { videos ->
                        Text(
                            text = "Official trailer",
                            modifier = Modifier.padding(start = 16.dp),
                            style = typography.titleLarge,
                            fontWeight = FontWeight.W600,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = TextColor,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        VideoPlayer(videoId = videos.key)
                    }
                }

                else -> Unit
            }
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = "Overview",
            modifier = Modifier.padding(start = 16.dp),
            style = typography.titleLarge,
            fontWeight = FontWeight.W600,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = TextColor,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.padding(8.dp))
        movieDetails.overview?.let { overview ->
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
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = "Reviews",
            modifier = Modifier.padding(start = 16.dp),
            style = typography.titleLarge,
            fontWeight = FontWeight.W600,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = TextColor,
            textAlign = TextAlign.Start
        )

        when (movieReviewsUiState) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Success -> {
                Spacer(modifier = Modifier.padding(16.dp))
                (movieReviewsUiState.data as? MovieReviews)?.reviews?.let { reviews ->
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(reviews) { review ->
                            MovieReviewCard(review = review)
                        }
                    }
                }
            }

            else -> Unit
        }

        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = "Recommendations",
            modifier = Modifier.padding(start = 16.dp),
            style = typography.titleLarge,
            fontWeight = FontWeight.W600,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = TextColor,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.padding(8.dp))
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            when (movieRecommendationsUiState) {
                is UiState.Loading -> {
                    item {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Success -> {
                    (movieRecommendationsUiState.data as? MoviesResponse)?.movies?.let { list ->
                        items(list) { result ->
                            SmallMovieCard(movie = result, onItemClicked = {
                                onItemClicked(result)
                            })
                        }
                    }
                }

                else -> {

                }
            }
        }
    }
}

@Composable
private fun MoviePoster(
    modifier: Modifier = Modifier, movieDetails: MovieDetails
) {

    Box(modifier = modifier.height(360.dp)) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
                .clip(RectangleShape)
                .align(Alignment.TopCenter),
            model = ImageRequest.Builder(LocalContext.current)
                .data("${Constants.Original_IMAGE_BASE_URL}${movieDetails.backdropPath}")
                .crossfade(true).build(),
            contentDescription = "Image"
        ) {
            when (painter.state) {
                is AsyncImagePainter.State.Loading -> {
                    Box(
                        Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                else -> {
                    SubcomposeAsyncImageContent(
                        modifier = Modifier.clip(RectangleShape), contentScale = ContentScale.Crop
                    )
                }
            }
        }
        SubcomposeAsyncImage(
            modifier = Modifier
                .padding(start = 8.dp)
                .size(120.dp)
                .clip(CircleShape)
                .align(Alignment.BottomStart)
                .border(5.dp, BackgroundColor, CircleShape),
            model = ImageRequest.Builder(LocalContext.current)
                .data("${Constants.Original_IMAGE_BASE_URL}${movieDetails.posterPath}")
                .crossfade(true).build(),
            contentDescription = "Image"
        ) {
            when (painter.state) {
                is AsyncImagePainter.State.Loading -> {
                    Box(
                        Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                else -> {
                    SubcomposeAsyncImageContent(
                        modifier = Modifier.clip(CircleShape), contentScale = ContentScale.Crop
                    )
                }
            }
        }
        Box(
            Modifier
                .align(Alignment.BottomEnd)
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .background(
                    Color.Black.copy(0.8f), shape = RoundedCornerShape(10.dp)
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
                    text = String.format("%.1f", movieDetails.voteAverage),
                    style = typography.bodyMedium,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
private fun VideoPlayer(
    videoId: String,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = {
            val view = YouTubePlayerView(it)
            view.addYouTubePlayerListener(
                object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        super.onReady(youTubePlayer)
                        youTubePlayer.loadVideo(
                            videoId,
                            0f
                        )
                    }
                }
            )
            view
        }, modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16 / 9f)
    )
}


@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
private fun MovieReviewCard(
    review: Review
) {
    var expanded by remember { mutableStateOf(false) }

    val transition = updateTransition(targetState = expanded, label = "expansion")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clip(RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor),
    ) {
        Column(
            modifier = Modifier
                .width(400.dp)
                .padding(16.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = { expanded = !expanded })
                .wrapContentHeight()
        ) {
            Text(
                text = review.author,
                style = typography.titleMedium,
                fontWeight = FontWeight.W600,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = TextColor,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = review.created_at,
                style = typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = review.content,
                style = typography.bodyMedium,
                fontWeight = FontWeight.W500,
                overflow = TextOverflow.Ellipsis,
                maxLines = if (expanded) 50 else 3,
                color = TextColor
            )
        }
    }
}

