package com.example.githubrepositoriesapp.presentation.githubrepositories

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.githubrepositoriesapp.data.model.GitHubRepositoryModel
import com.example.githubrepositoriesapp.data.repository.GitHubListRepository
import kotlinx.coroutines.flow.Flow

class GitHubRepositoriesFragmentViewModel(private val gitHubListRepository: GitHubListRepository) :
    ViewModel() {

    val showData: Flow<PagingData<GitHubRepositoryModel>> by lazy { gitHubListRepository.pagingData() }

}