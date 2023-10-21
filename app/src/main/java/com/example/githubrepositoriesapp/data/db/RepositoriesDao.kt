package com.example.githubrepositoriesapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class RepositoriesDao {

    @Transaction
    @Query("""
        SELECT repositories.* FROM repositories
        WHERE repositories.repositoriesTag = :tag
        ORDER BY repositories.id
        LIMIT :limit
        OFFSET :offset
    """)
    abstract suspend fun findRepositories(offset: Int, limit: Int, tag: String): List<RepositoriesEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertRepositories(repositories: List<RepositoriesEntity>)

    @Query("DELETE FROM repositories WHERE repositoriesTag = :tag")
    abstract suspend fun deleteRepositoriesByTag(tag: String)

    @Query("DELETE FROM repositories")
    abstract suspend fun deleteRepositories()

    @Query("SELECT repositories.* FROM repositories WHERE repositories.repositoriesTag = :tag AND repositories.repositoriesId = :itemId")
    abstract suspend fun getRepositoryByTagAndId(tag: String, itemId: Int): RepositoriesEntity

}