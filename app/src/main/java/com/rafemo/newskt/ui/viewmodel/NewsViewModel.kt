package com.rafemo.newskt.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafemo.newskt.api.ErrorCode
import com.rafemo.newskt.api.Resource
import com.rafemo.newskt.model.Article
import com.rafemo.newskt.model.NewsResponse
import com.rafemo.newskt.repository.NewsRepository
import com.rafemo.newskt.util.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage: Int = 1
    var breakingNewsResponse: NewsResponse? = null


    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage: Int = 1
    var searchNewsResponse: NewsResponse? = null

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = it
                } else {
                    val oldArticle = breakingNewsResponse?.articles
                    val newArticles = it.articles
                    oldArticle?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                searchNewsPage++
                if (breakingNewsResponse == null) {
                    searchNewsResponse = it
                } else {
                    val oldArticle = searchNewsResponse?.articles
                    val newArticles = it.articles
                    oldArticle?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.insert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        newsRepository.deleteArticle(article)
    }

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(context)) {
                val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
                viewModelScope.launch {
                    // Remove splash screen :D
                    _isLoading.value = false
                }
            } else {
                breakingNews.postValue(Resource.Error(ErrorCode.NO_INTERNET.message))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error(ErrorCode.NETWORK_FAILURE.message))
                else -> breakingNews.postValue(Resource.Error(ErrorCode.CONVERSION_FAILED.message))
            }
        }
    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        searchNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(context)) {
                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error(ErrorCode.NO_INTERNET.message))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(Resource.Error(ErrorCode.NETWORK_FAILURE.message))
                else -> searchNews.postValue(Resource.Error(ErrorCode.CONVERSION_FAILED.message))
            }
        }
    }

}