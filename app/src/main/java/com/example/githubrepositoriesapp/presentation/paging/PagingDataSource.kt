package com.example.githubrepositoriesapp.presentation.paging

import androidx.paging.PagingSource
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.example.githubrepositoriesapp.data.model.GitHubRepositoryModel
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first

class PagingDataSource(
    private val tag: String,
    private val sort: String,
    private val store: Store<RepositoriesPagingConfig, List<GitHubRepositoryModel>>
) : PagingSource<Int, GitHubRepositoryModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GitHubRepositoryModel> {
        val page = params.key ?: 1
        val config = RepositoriesPagingConfig(page = page, perPage = params.loadSize, tag = tag, sort = sort)
        val storeRequest = StoreRequest.skipMemory(config, refresh = false)

        return try {
            val repositories = store.stream(storeRequest)
                .filterNot { it is StoreResponse.Loading }
                .first()
                .requireData()

            LoadResult.Page(data = repositories, prevKey = null, nextKey = page + 1)
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }
}
