package com.hocel.moviedb.presentation.movieDetails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hocel.moviedb.data.models.movieDetails.MovieDetailsResponse
import com.hocel.moviedb.data.models.trendingMovies.MoviesResponse
import com.hocel.moviedb.data.repository.Repository
import com.hocel.moviedb.utils.uiState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _movieUiState: MutableStateFlow<UiState?> = MutableStateFlow(null)
    val movieUiState: StateFlow<UiState?> get() = _movieUiState

    private val _movieRecommendationUiState: MutableStateFlow<UiState?> = MutableStateFlow(null)
    val movieRecommendationUiState: StateFlow<UiState?> get() = _movieRecommendationUiState

    private val _movieDetails: MutableState<MovieDetailsResponse?> = mutableStateOf(null)
    val movieDetails: State<MovieDetailsResponse?> get() = _movieDetails

    private val _movieRecommendations: MutableState<MoviesResponse?> = mutableStateOf(null)
    val movieRecommendations: State<MoviesResponse?> get() = _movieRecommendations


    fun getMovieDetailsData(movieId: Int) {
        viewModelScope.launch {
            try {
                _movieUiState.value = UiState.Loading

                val response = repository.getMovieDetails(movieId)

                if (response.isSuccessful) {
                    _movieUiState.value = UiState.Success
                    _movieDetails.value = response.body()
                } else {
                    _movieUiState.value = UiState.Fail
                }

            } catch (e: Exception) {
                _movieUiState.value = UiState.Fail
            }
        }
    }

    fun getMovieRecommendations(movieId: Int) {
        viewModelScope.launch {
            try {
                _movieRecommendationUiState.value = UiState.Loading

                val response = repository.getMovieRecommendations(movieId)

                if (response.isSuccessful) {
                    _movieRecommendationUiState.value = UiState.Success
                    _movieRecommendations.value = response.body()
                } else {
                    _movieRecommendationUiState.value = UiState.Fail
                }

            } catch (e: Exception) {
                _movieRecommendationUiState.value = UiState.Fail
            }
        }
    }

}