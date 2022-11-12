package com.rafemo.newskt.repository

import com.rafemo.newskt.api.RetrofitInstance
import com.rafemo.newskt.db.ArticleDatabase
import retrofit2.Retrofit

class NewsRepository(
    val db: ArticleDatabase
) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)


}