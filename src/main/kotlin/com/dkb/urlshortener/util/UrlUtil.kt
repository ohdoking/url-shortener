package com.dkb.urlshortener.util

import jakarta.servlet.http.HttpServletRequest

object UrlUtil {
    fun buildShortUrl(request: HttpServletRequest, alias: String): String {
        val domain = request.getAttribute("currentDomain") as? String ?: ""
        return "$domain/$alias"
    }
}