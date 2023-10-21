package com.example.githubrepositoriesapp.di

import com.example.githubrepositoriesapp.common.BASE_URL
import com.example.githubrepositoriesapp.data.StoreModule
import com.example.githubrepositoriesapp.data.db.AppDatabase
import com.example.githubrepositoriesapp.data.network.GitHubRepositoryApi
import com.example.githubrepositoriesapp.data.repository.GitHubListRepository
import com.example.githubrepositoriesapp.data.repository.GitHubListRepositoryImpl
import com.example.githubrepositoriesapp.presentation.githubrepositories.GitHubRepositoriesFragmentViewModel
import com.example.githubrepositoriesapp.presentation.githubrepositorydetails.DetailsFragmentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private val apiModule = module {

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<GitHubRepositoryApi> { get<Retrofit>().create(GitHubRepositoryApi::class.java) }

}

private val repositoryModule = module {
    single<GitHubListRepository> { GitHubListRepositoryImpl(get(), get()) }
}

private val databaseModule = module {
    single { AppDatabase.getInstance(androidContext()) }
    single { get<AppDatabase>().repositoriesDao() }
    single { StoreModule.provideStore(get(), get()) }
}

private val viewModelModule = module {
    viewModel { GitHubRepositoriesFragmentViewModel(get()) }
    viewModel { DetailsFragmentViewModel(get()) }
}

val all = listOf(
    apiModule,
    viewModelModule,
    repositoryModule,
    databaseModule
)

