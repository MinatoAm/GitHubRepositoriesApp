package com.example.githubrepositoriesapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dropbox.android.external.store4.Store
import com.example.githubrepositoriesapp.common.ActionResult
import com.example.githubrepositoriesapp.common.PAGE_SIZE
import com.example.githubrepositoriesapp.common.PREFETCH_DISTANCE
import com.example.githubrepositoriesapp.common.SEARCH_QUERY
import com.example.githubrepositoriesapp.common.SORT
import com.example.githubrepositoriesapp.data.db.RepositoriesDao
import com.example.githubrepositoriesapp.data.db.RepositoriesEntity.Companion.toRepository
import com.example.githubrepositoriesapp.data.model.GitHubRepositoryModel
import com.example.githubrepositoriesapp.presentation.paging.PagingDataSource
import com.example.githubrepositoriesapp.presentation.paging.RepositoriesPagingConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface GitHubListRepository {
    fun pagingData(): Flow<PagingData<GitHubRepositoryModel>>
    suspend fun getRepositoryByTagAndId(
        tag: String,
        id: Int
    ): Flow<ActionResult<GitHubRepositoryModel>>
}

class GitHubListRepositoryImpl(
    private val repositoriesDao: RepositoriesDao,
    private val store: Store<RepositoriesPagingConfig, List<GitHubRepositoryModel>>
) : GitHubListRepository {

    @Suppress("OPT_IN_USAGE_FUTURE_ERROR")
    override fun pagingData(): Flow<PagingData<GitHubRepositoryModel>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = PREFETCH_DISTANCE,
            initialLoadSize = PAGE_SIZE,
            enablePlaceholders = false
        ),
        initialKey = 1
    ) {
        PagingDataSource(SEARCH_QUERY, SORT, store)
    }.flow

    override suspend fun getRepositoryByTagAndId(
        tag: String,
        id: Int
    ): Flow<ActionResult<GitHubRepositoryModel>> = flow {
        emit(ActionResult.Loading())
        try {
            val data = repositoriesDao.getRepositoryByTagAndId(tag, id).toRepository()
            emit(ActionResult.Success(data))
        } catch (e: Exception) {
            emit(ActionResult.Error(e.message ?: ""))
        }
    }
}