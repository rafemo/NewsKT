package com.rafemo.newskt.di

import android.content.Context
import androidx.room.Room
import com.rafemo.newskt.db.ArticleDAO
import com.rafemo.newskt.db.ArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ArticleDatabaseModule {

    @Provides
    fun provideChannelDao(articleDatabase: ArticleDatabase): ArticleDAO {
        return articleDatabase.articleDao()
    }

    @Provides
    @Singleton
    fun provideArticleDatabase(@ApplicationContext appContext: Context): ArticleDatabase {
        return Room.databaseBuilder(
            appContext,
            ArticleDatabase::class.java,
            ArticleDatabase.DB_NAME
        ).build()
    }

}