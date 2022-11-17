package com.rafemo.newskt.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*


fun hasInternetConnection(context: Context): Boolean {

    val manager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val activeNetwork = manager.activeNetwork ?: return false
    val capabilities = manager.getNetworkCapabilities(activeNetwork) ?: return false

    return when {
        capabilities.hasTransport(TRANSPORT_WIFI) -> true
        capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
        capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
        else -> false
    }
}