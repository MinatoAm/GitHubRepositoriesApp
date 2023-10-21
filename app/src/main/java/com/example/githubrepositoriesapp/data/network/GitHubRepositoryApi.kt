package com.example.githubrepositoriesapp.data.network

import com.example.githubrepositoriesapp.data.model.GitHubRepositoryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubRepositoryApi {

    @GET("/search/repositories")
    suspend fun getRepositories(@Query("q") q: String,
                                @Query("page") page: Int,
                                @Query("per_page") perPage: Int,
                                @Query("sort") sort: String): GitHubRepositoryResponse
}