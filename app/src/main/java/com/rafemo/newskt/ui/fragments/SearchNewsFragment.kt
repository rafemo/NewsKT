package com.rafemo.newskt.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rafemo.newskt.R
import com.rafemo.newskt.api.Resource
import com.rafemo.newskt.ui.NewsActivity
import com.rafemo.newskt.ui.adapters.NewsAdapter
import com.rafemo.newskt.ui.viewmodel.NewsViewModel
import com.rafemo.newskt.util.Constants
import com.rafemo.newskt.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_breaking_news.paginationProgressBar
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : BaseNewsFragment(R.layout.fragment_search_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
        }

        var job: Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideLoading()
                    response.data?.let { newsResponse ->
                        // Remember: Differ can't work with mutable lists!
                        newsAdapter.differ.submitList(newsResponse.articles.toList())

                        // Check if it's last page available
                        val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.searchNewsPage == totalPages


                        if (isLastPage) {
                            // Reset the padding, let space to progressBar
                            rvBreakingNews.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideLoading()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error ocurred: $message", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "An error ocurred: $message")
                    }
                }
                is Resource.Loading -> showLoading()
            }
        })
    }

    override fun hideLoading() {
        super.hideLoading()
        paginationProgressBar.visibility = View.INVISIBLE
    }

    override fun showLoading() {
        super.showLoading()
        paginationProgressBar.visibility = View.VISIBLE
    }

    override val onScrolledAction: () -> Unit = {
        viewModel.searchNews(etSearch.text.toString())
        isScrolling = false
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.scrollListener)
        }
    }

}