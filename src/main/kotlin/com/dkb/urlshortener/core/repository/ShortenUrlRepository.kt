package com.dkb.urlshortener.core.repository

import com.dkb.urlshortener.core.model.ShortenUrlDao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ShortenUrlRepository : JpaRepository<ShortenUrlDao, UUID> {

    fun findByAlias(alias: String): ShortenUrlDao?
    fun findByOriginalUrl(originalUrl: String): ShortenUrlDao?
}