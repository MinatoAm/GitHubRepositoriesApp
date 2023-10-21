package com.example.githubrepositoriesapp.presentation.githubrepositorydetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubrepositoriesapp.common.ActionResult
import com.example.githubrepositoriesapp.common.SEARCH_QUERY
import com.example.githubrepositoriesapp.data.repository.GitHubListRepository
import com.example.githubrepositoriesapp.presentation.githubrepositorydetails.model.DetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DetailsFragmentViewModel(private val gitHubListRepository: GitHubListRepository) :
    ViewModel() {

    private val _detailsUiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val detailsUiState: StateFlow<DetailsUiState> = _detailsUiState

    fun getRepositoryByTagAndId(id: Int) =
        viewModelScope.launch {
            gitHubListRepository.getRepositoryByTagAndId(SEARCH_QUERY, id).onEach { result ->
                when (result) {
                    is ActionResult.Loading -> {
                        _detailsUiState.value = DetailsUiState.Loading
                    }

                    is ActionResult.Success -> {
                        _detailsUiState.value = DetailsUiState.Success(result.data)
                    }

                    is ActionResult.Error -> {
                        _detailsUiState.value = DetailsUiState.Error(result.message)
                    }
                }
            }.launchIn(this)
        }
}