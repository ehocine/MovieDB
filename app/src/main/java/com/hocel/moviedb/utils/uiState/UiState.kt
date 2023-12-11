package com.hocel.moviedb.utils.uiState

sealed class UiState {

    data object Loading : UiState()
    data object Success : UiState()
    data object Fail : UiState()
}