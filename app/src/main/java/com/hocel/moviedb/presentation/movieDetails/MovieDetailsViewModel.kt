package com.hocel.moviedb.presentation.movieDetails

import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.hocel.moviedb.data.repository.RemoteRepository
import com.hocel.moviedb.utils.uiState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
) : ViewModel() {

    private val _movieDetailsUiState: MutableState<UiState> = mutableStateOf(UiState.Idle)
    val movieDetailsUiState: State<UiState> get() = _movieDetailsUiState

    private val _movieRecommendationUiState: MutableState<UiState> =
        mutableStateOf(UiState.Idle)
    val movieRecommendationUiState: State<UiState> get() = _movieRecommendationUiState

    private val _movieVideosUiState: MutableState<UiState> = mutableStateOf(UiState.Idle)
    val movieVideosUiState: State<UiState> get() = _movieVideosUiState

    private val _movieReviewsUiState: MutableState<UiState> = mutableStateOf(UiState.Idle)
    val movieReviewsUiState: State<UiState> get() = _movieReviewsUiState

    fun getMovieDetailsData(movieId: Int) {
        viewModelScope.launch {
            try {
                _movieDetailsUiState.value = UiState.Loading

                val response = remoteRepository.getMovieDetails(movieId)

                if (response.isSuccessful) {
                    _movieDetailsUiState.value = UiState.Success(data = response.body())
                } else {
                    _movieDetailsUiState.value = UiState.Fail(message = response.message())
                }

            } catch (e: Exception) {
                _movieDetailsUiState.value = UiState.Fail(message = e.message)
            }
        }
    }

    fun getMovieRecommendations(movieId: Int) {
        viewModelScope.launch {
            try {
                _movieRecommendationUiState.value = UiState.Loading

                val response = remoteRepository.getMovieRecommendations(movieId)

                if (response.isSuccessful) {
                    _movieRecommendationUiState.value = UiState.Success(response.body())
                } else {
                    _movieRecommendationUiState.value = UiState.Fail(response.message())
                }

            } catch (e: Exception) {
                _movieRecommendationUiState.value = UiState.Fail(e.message)
            }
        }
    }

    fun getMovieVideos(movieId: Int) {
        viewModelScope.launch {
            try {
                _movieRecommendationUiState.value = UiState.Loading

                val response = remoteRepository.getMovieVideos(movieId)

                if (response.isSuccessful) {
                    _movieVideosUiState.value =
                        UiState.Success(response.body()?.movieVideoDetails?.first { it.type == "Trailer" })
                } else {
                    _movieVideosUiState.value = UiState.Fail(response.message())
                }

            } catch (e: Exception) {
                _movieVideosUiState.value = UiState.Fail(e.message)
            }
        }
    }

    fun getMovieReviews(movieId: Int) {
        viewModelScope.launch {
            try {
                _movieReviewsUiState.value = UiState.Loading

                val response = remoteRepository.getMovieReviews(movieId)

                if (response.isSuccessful) {
                    _movieReviewsUiState.value =
                        UiState.Success(response.body())
                } else {
                    _movieReviewsUiState.value = UiState.Fail(response.message())
                }

            } catch (e: Exception) {
                _movieReviewsUiState.value = UiState.Fail(e.message)
            }
        }
    }
}