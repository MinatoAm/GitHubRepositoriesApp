package com.example.githubrepositoriesapp.data.model


import com.google.gson.annotations.SerializedName

data class GitHubRepositoryResponse(
    @SerializedName("items")
    val items: List<GitHubRepositoryModel>?,
)

data class GitHubRepositoryModel(
    @SerializedName("description")
    val description: String?,
    @SerializedName("forks_count")
    val forksCount: Int?,
    @SerializedName("html_url")
    val url: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("language")
    val language: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("owner")
    val owner: Owner?,
    @SerializedName("stargazers_count")
    val stars: Int?,
)

data class Owner(
    @SerializedName("avatar_url")
    val avatarUrl: String?,
)