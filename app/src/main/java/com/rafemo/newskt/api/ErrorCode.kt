package com.rafemo.newskt.api

enum class ErrorCode(code: Int) {
    CONVERSION_FAILED(1),
    NO_INTERNET(500),
    NETWORK_FAILURE(500);

    val message: String
        get() = when(this) {
            CONVERSION_FAILED -> "Conversion error"
            NO_INTERNET -> "No internet connection"
            NETWORK_FAILURE -> "Network failure"
        }
}