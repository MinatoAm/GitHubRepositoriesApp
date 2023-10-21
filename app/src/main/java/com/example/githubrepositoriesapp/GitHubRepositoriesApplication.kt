package com.example.githubrepositoriesapp

import android.app.Application
import com.example.githubrepositoriesapp.di.all
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GitHubRepositoriesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@GitHubRepositoriesApplication)
            modules(all)
        }
    }
}