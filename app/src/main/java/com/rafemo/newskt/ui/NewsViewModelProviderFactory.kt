package com.rafemo.newskt.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rafemo.newskt.repository.NewsRepository
import com.rafemo.newskt.ui.viewmodel.NewsViewModel

class NewsViewModelProviderFactory(
    val app: Application,
    private val newsRepository: NewsRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app, newsRepository) as T
    }
}