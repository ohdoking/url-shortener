package com.dkb.urlshortener.util

import jakarta.servlet.http.HttpServletRequest

/**
 * Utility functions for building URLs.
 */
object UrlUtil {

    /**
     * Constructs short URL using the domain from request attributes and alias.
     */
    fun buildShortUrl(request: HttpServletRequest, alias: String): String {
        val domain = request.getAttribute("currentDomain") as? String ?: ""
        return "$domain/$alias"
    }
}