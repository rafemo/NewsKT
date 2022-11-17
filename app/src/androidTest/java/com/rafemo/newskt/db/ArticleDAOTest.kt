package com.rafemo.newskt.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.rafemo.newskt.getOrAwaitValue
import com.rafemo.newskt.model.Article
import com.rafemo.newskt.model.Source
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ArticleDAOTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ArticleDatabase
    private lateinit var dao: ArticleDAO

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ArticleDatabase::class.java
        ).allowMainThreadQueries() // Don't allow multi-threading
            .build()
        dao = database.articleDao()
    }

    @After
    fun teardown() {
        database.close()
    }


    @Test
    fun insertArticle() = runTest {
        val article = Article(
            id = 0,
            "author",
            "content",
            "Description",
            "publishedAt",
            Source("name", "name"), // Converter stores id like name value
            "title",
            "url",
            "urlToImage")

        dao.insertArticle(article)

        val allArticles = dao.getAllArticles().getOrAwaitValue()

        assertThat(allArticles).contains(article)
    }

    @Test
    fun deleteArticle() = runTest {
        val article = Article(
            id = 0,
            "author",
            "content",
            "Description",
            "publishedAt",
            Source("name", "name"), // Converter stores id like name value
            "title",
            "url",
            "urlToImage")

        dao.insertArticle(article)
        dao.deleteArticle(article)

        val allArticles = dao.getAllArticles().getOrAwaitValue()

        assertThat(allArticles).doesNotContain(article)
    }

}