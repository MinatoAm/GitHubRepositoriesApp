package com.example.githubrepositoriesapp.data

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.example.githubrepositoriesapp.common.SORT
import com.example.githubrepositoriesapp.data.db.RepositoriesDao
import com.example.githubrepositoriesapp.data.db.RepositoriesEntity
import com.example.githubrepositoriesapp.data.db.RepositoriesEntity.Companion.toRepository
import com.example.githubrepositoriesapp.data.model.GitHubRepositoryModel
import com.example.githubrepositoriesapp.data.network.GitHubRepositoryApi
import com.example.githubrepositoriesapp.presentation.paging.RepositoriesPagingConfig
import kotlinx.coroutines.flow.flow

object StoreModule {

    fun provideStore(
        gitHubRepositoryApi: GitHubRepositoryApi,
        repositoriesDao: RepositoriesDao
    ): Store<RepositoriesPagingConfig, List<GitHubRepositoryModel>> {
        return StoreBuilder.from<RepositoriesPagingConfig, List<GitHubRepositoryModel>, List<GitHubRepositoryModel>>(
            Fetcher.of { config ->
                gitHubRepositoryApi.getRepositories(
                    config.tag,
                    config.page,
                    config.perPage,
                    SORT
                ).items
                    ?: emptyList()
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { config ->
                    flow {
                        val offset = (config.page - 1) * config.perPage
                        val repositories =
                            repositoriesDao.findRepositories(offset, config.perPage, config.tag)
                        // Return null, then store fetch data using fetcher.
                        emit(if (repositories.isEmpty()) null else repositories.map { it.toRepository() })
                    }
                },
                writer = { config, repositories ->
                    repositoriesDao.insertRepositories(
                        repositories.map { repository ->
                            RepositoriesEntity(
                                0,
                                repository.id,
                                repository.name,
                                repository.description,
                                repository.url,
                                repository.language,
                                repository.owner?.avatarUrl,
                                repository.stars,
                                repository.forksCount,
                                config.tag
                            )
                        }
                    )
                },
                delete = { config ->
                    repositoriesDao.deleteRepositoriesByTag(config.tag)
                },
                deleteAll = {
                    repositoriesDao.deleteRepositories()
                }
            )
        ).disableCache().build()
    }
}