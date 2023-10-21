package com.example.githubrepositoriesapp.data.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.githubrepositoriesapp.data.model.GitHubRepositoryModel
import com.example.githubrepositoriesapp.data.model.Owner

@Entity(
    tableName = "repositories",
    indices = [Index("repositoriesId", "repositoriesTag", unique = true), Index("repositoriesTag")]
)
data class RepositoriesEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val repositoriesId: Int?,
    val name: String?,
    val description: String?,
    val url: String?,
    val language: String?,
    val avatarUrl: String?,
    val stars: Int?,
    val forksCount: Int?,
    val repositoriesTag: String
) {
    companion object {
        fun RepositoriesEntity.toRepository() =
            GitHubRepositoryModel(
                id = repositoriesId,
                name = name,
                description = description,
                url = url,
                language = language,
                owner = Owner(avatarUrl),
                stars = stars,
                forksCount = forksCount
            )
    }
}
