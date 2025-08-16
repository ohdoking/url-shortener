package com.dkb.urlshortener.core.service

import com.dkb.urlshortener.core.model.ShortenUrlDao

interface UrlService {
    fun createShortenUrl(originalUrl: String): ShortenUrlDao
    fun getOriginalUrlByAlias(alias: String): ShortenUrlDao
}