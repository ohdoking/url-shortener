package com.dkb.urlshortener.api.controller

import com.dkb.urlshortener.api.dto.CreateUrlRequest
import com.dkb.urlshortener.api.dto.ShortUrlResponse
import com.dkb.urlshortener.core.service.UrlService
import com.dkb.urlshortener.util.UrlUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/urls")
class UrlApiController(
    private val urlService: UrlService
) {

    private val logger: Logger = LoggerFactory.getLogger(UrlApiController::class.java)


    @PostMapping
    fun createShortUrl(
        @Valid @RequestBody requestDto: CreateUrlRequest,
        httpRequest: HttpServletRequest
    ): ResponseEntity<ShortUrlResponse> {
        logger.info("Received request to shorten URL: {}", requestDto.originalUrl)
        val dao = urlService.createShortenUrl(requestDto.originalUrl)
        val shortUrl = UrlUtil.buildShortUrl(httpRequest, dao.alias)
        val response = ShortUrlResponse.fromDao(dao, shortUrl)
        logger.info("Short URL created successfully: {} -> {}", dao.alias, shortUrl)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/{alias}")
    fun getOriginalUrl(
        @PathVariable alias: String,
        httpRequest: HttpServletRequest
    ): ResponseEntity<ShortUrlResponse> {
        logger.info("Received request to get original URL for alias: {}", alias)
        val dao = urlService.getOriginalUrlByAlias(alias)
        val shortUrl = UrlUtil.buildShortUrl(httpRequest, dao.alias)
        val response = ShortUrlResponse.fromDao(dao, shortUrl)
        logger.info("Original URL retrieved successfully for alias: {}", alias)
        return ResponseEntity.ok(response)
    }

}