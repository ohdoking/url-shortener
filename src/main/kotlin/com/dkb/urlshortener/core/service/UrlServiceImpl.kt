package com.dkb.urlshortener.core.service

import com.dkb.urlshortener.core.exception.AliasNotFoundException
import com.dkb.urlshortener.core.model.ShortenUrlDao
import com.dkb.urlshortener.core.repository.ShortenUrlRepository
import com.dkb.urlshortener.util.BaseEncoder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Service


@Service
class UrlServiceImpl(
    private val repository: ShortenUrlRepository,
    private val baseEncoder: BaseEncoder,
    private val cacheManager: CacheManager
) : UrlService {

    private val logger: Logger = LoggerFactory.getLogger(UrlServiceImpl::class.java)

    override fun createShortenUrl(originalUrl: String): ShortenUrlDao {
        repository.findByOriginalUrl(originalUrl)?.let {
            logger.info("Original URL already shortened: {} -> {}", originalUrl, it.alias)
            return it
        }

        var alias: String
        do {
            alias = baseEncoder.encode(originalUrl)
        } while (repository.findByAlias(alias) != null)

        val dao = ShortenUrlDao(alias = alias, originalUrl = originalUrl)
        val savedDao = repository.save(dao)
        logger.info("Created new short URL: {} -> {}", originalUrl, alias)
        return savedDao
    }

    override fun getOriginalUrlByAlias(alias: String): ShortenUrlDao {

        cacheManager.getCache("urlCache")?.get(alias, ShortenUrlDao::class.java)?.let {
            logger.info("Cache hit for alias: {}", alias)
            return it
        }

        val dao = repository.findByAlias(alias) ?: throw AliasNotFoundException(alias)
        logger.info("Cache miss for alias: {}", alias)
        cacheManager.getCache("urlCache")?.put(alias, dao)
        return dao
    }
}

