package com.example.githubrepositoriesapp.presentation.githubrepositorydetails.model

import com.example.githubrepositoriesapp.data.model.GitHubRepositoryModel

sealed class DetailsUiState {
    data class Success(val data: GitHubRepositoryModel?): DetailsUiState()
    data class Error(val errorMessage: String?): DetailsUiState()
    data object Loading: DetailsUiState()
}
