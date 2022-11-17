package com.rafemo.newskt.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.rafemo.newskt.R
import com.rafemo.newskt.ui.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_news.*

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    private val viewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Show splash screen
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                // As long we are loading news, keep splash
                viewModel.isLoading.value
            }
        }

        setDefaultStyle()

        setContentView(R.layout.activity_news)

        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }

    private fun setDefaultStyle() {
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        delegate.applyDayNight()
    }

}