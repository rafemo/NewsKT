package com.rafemo.newskt.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
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
import com.rafemo.newskt.util.Constants.Companion.QUERY_PAGE_SIZE
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class BreakingNewsFragment : BaseNewsFragment(R.layout.fragment_breaking_news) {

    private lateinit var viewModel: NewsViewModel
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
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideLoading()
                    Log.e(TAG, "An error ocurred:AAAAAAAAAA")
                    response.data?.let { newsResponse ->
                        // Remember: Differ can't work with mutable lists!
                        newsAdapter.differ.submitList(newsResponse.articles.toList())

                        // Check if it's last page available
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages

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
        viewModel.getBreakingNews("us")
        isScrolling = false
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }

}