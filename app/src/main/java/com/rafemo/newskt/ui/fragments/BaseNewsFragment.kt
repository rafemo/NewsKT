package com.rafemo.newskt.ui.fragments

import android.widget.AbsListView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rafemo.newskt.util.Constants

interface IBaseNewsFragment {

    val onScrolledAction: (() -> Unit)

    fun hideLoading()
    fun showLoading()
}

abstract class BaseNewsFragment(@LayoutRes val contentLayoutId: Int) : Fragment(contentLayoutId), IBaseNewsFragment {

    val TAG: String = this::class.simpleName.toString()

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

     override fun hideLoading() {
        isLoading = false
    }

    override fun showLoading() {
        isLoading = true
    }

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                onScrolledAction.invoke()
            }

        }
    }

}