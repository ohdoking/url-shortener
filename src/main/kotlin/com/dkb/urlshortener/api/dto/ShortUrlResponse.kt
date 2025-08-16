package com.dkb.urlshortener.api.dto

import com.dkb.urlshortener.core.model.ShortenUrlDao

data class ShortUrlResponse(
    val alias: String,
    val shortUrl: String,
    val originalUrl: String
){
    companion object {
        fun fromDao(dao: ShortenUrlDao, shortUrl: String): ShortUrlResponse {
            return ShortUrlResponse(
                alias = dao.alias,
                originalUrl = dao.originalUrl,
                shortUrl = shortUrl
            )
        }
    }
}