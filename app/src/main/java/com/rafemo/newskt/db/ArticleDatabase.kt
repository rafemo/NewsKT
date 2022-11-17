package com.rafemo.newskt.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rafemo.newskt.model.Article

@Database(entities = [Article::class], version = 1)
@TypeConverters(Converters::class)
abstract class ArticleDatabase: RoomDatabase() {

    abstract fun articleDao(): ArticleDAO

    companion object {
        const val DB_NAME = "article_db.db"
    }

}