package com.dkb.urlshortener.core.service

import com.dkb.urlshortener.core.exception.AliasNotFoundException
import com.dkb.urlshortener.core.model.ShortenUrlDao
import com.dkb.urlshortener.core.repository.ShortenUrlRepository
import com.dkb.urlshortener.util.BaseEncoder
import org.springframework.stereotype.Service
import kotlin.io.encoding.Base64


@Service
class UrlServiceImpl(
    private val repository: ShortenUrlRepository,
    private val base62Encoder: BaseEncoder
) : UrlService {

    override fun createShortenUrl(originalUrl: String): ShortenUrlDao {
        repository.findByOriginalUrl(originalUrl)?.let { return it }

        var alias: String
        do {
            alias = base62Encoder.encode(originalUrl)
        } while (repository.findByAlias(alias) != null)

        val dao = ShortenUrlDao(alias = alias, originalUrl = originalUrl)
        return repository.save(dao)
    }

    override fun getOriginalUrlByAlias(alias: String): ShortenUrlDao {
        return repository.findByAlias(alias) ?: throw AliasNotFoundException(alias)
    }
}

