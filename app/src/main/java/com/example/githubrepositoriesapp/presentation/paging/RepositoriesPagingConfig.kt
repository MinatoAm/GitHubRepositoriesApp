package com.example.githubrepositoriesapp.presentation.paging

data class RepositoriesPagingConfig(
    val page: Int,
    val perPage: Int,
    val tag: String,
    val sort: String
)