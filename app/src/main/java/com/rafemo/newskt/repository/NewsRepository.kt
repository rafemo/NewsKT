package com.rafemo.newskt.repository

import com.rafemo.newskt.api.NewsAPI
import com.rafemo.newskt.db.ArticleDAO
import com.rafemo.newskt.model.Article
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NewsRepository @Inject constructor(
    private val api: NewsAPI,
    private val articleDao: ArticleDAO
) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        api.searchForNews(searchQuery, pageNumber)

    suspend fun insert(article: Article) = articleDao.insertArticle(article)

    fun getSavedNews() = articleDao.getAllArticles()

    fun deleteArticle(article: Article) = articleDao.deleteArticle(article)

}