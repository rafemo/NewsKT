package com.rafemo.newskt.repository

import com.rafemo.newskt.api.RetrofitInstance
import com.rafemo.newskt.db.ArticleDatabase
import com.rafemo.newskt.model.Article
import retrofit2.Retrofit

class NewsRepository(
    private val db: ArticleDatabase
) {

    /*
    About suspend fun :)
     1. If it returns livedata/flow it's not a suspend function because that
     already contains the async behavior itself.
     2. If you directly get a result of the db or insert/delete, it's a suspend fun
    */

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun insert(article: Article) = db.articleDao().insert(article)

    fun getSavedNews() = db.articleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.articleDao().deleteArticle(article)

}