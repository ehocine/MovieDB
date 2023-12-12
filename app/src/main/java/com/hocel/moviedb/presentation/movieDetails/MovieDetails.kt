package com.hocel.moviedb.presentation.movieDetails

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.hocel.moviedb.ui.theme.BackgroundColor
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
    }

    val context = LocalContext.current as ComponentActivity
    val window = context.window
    WindowCompat.setDecorFitsSystemWindows(window, false)
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
//    windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    windowInsetsController.let {
        it.hide(WindowInsetsCompat.Type.statusBars())
        it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }


    val movieDetails by remember { viewModel.movieDetails }

    val uiState by viewModel.movieUiState.collectAsState()
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {

        when (uiState) {
            is UiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Success -> {
                movieDetails?.let {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(BackgroundColor)
                    ) {
                        Box(Modifier.fillMaxWidth()) {
                            SubcomposeAsyncImage(
                                modifier = Modifier
                                    .height(400.dp)
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

                            Box(
                                Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
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
                        Column(Modifier.fillMaxSize()) {
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
                            Column(
                                Modifier
                                    .fillMaxSize()
                                    .padding(bottom = 80.dp)
                                    .verticalScroll(state = scrollState)
                            ) {
                                it.overview?.let { overview ->
                                    Text(
                                        text = overview,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp, 0.dp, 16.dp, 0.dp),
                                        color = TextColor,
                                        fontWeight = FontWeight.W600,
                                        style = typography.bodyLarge,
                                        textAlign = TextAlign.Start
                                    )
                                }
                            }
                        }
                    }
                }

                //something worng
            }

            else -> {

            }
        }
    }
}


