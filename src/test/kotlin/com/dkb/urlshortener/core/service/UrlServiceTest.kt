package com.dkb.urlshortener.core.service

import com.dkb.urlshortener.core.exception.AliasNotFoundException
import com.dkb.urlshortener.core.model.ShortenUrlDao
import com.dkb.urlshortener.core.repository.ShortenUrlRepository
import com.dkb.urlshortener.util.BaseEncoder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class UrlServiceImplTest {

    private lateinit var repository: ShortenUrlRepository
    private lateinit var baseEncoder: BaseEncoder
    private lateinit var urlService: UrlServiceImpl

    @BeforeEach
    fun setUp() {
        repository = mock()
        baseEncoder = mock()
        urlService = UrlServiceImpl(repository, baseEncoder)
    }

    @Nested
    @DisplayName("createShortenUrl")
    inner class CreateShortenUrlTest {

        @Test
        @DisplayName("Given an existing originalUrl When createShortenUrl Then should return existing DAO")
        fun givenExistingOriginalUrl_whenCreateShortenUrl_thenReturnExistingDao() {
            val existingDao = ShortenUrlDao(alias = "abc123", originalUrl = "https://dkb.com")
            whenever(repository.findByOriginalUrl("https://dkb.com")).thenReturn(existingDao)

            val result = urlService.createShortenUrl("https://dkb.com")

            assertThat(result).isEqualTo(existingDao)
        }

        @Test
        @DisplayName("Given a new originalUrl When createShortenUrl Then should generate alias and save new DAO")
        fun givenNewOriginalUrl_whenCreateShortenUrl_thenGenerateAliasAndSave() {
            whenever(repository.findByOriginalUrl("https://dkb.com")).thenReturn(null)
            whenever(baseEncoder.encode(any<String>())).thenReturn("abc123")
            whenever(repository.findByAlias("abc123")).thenReturn(null)
            whenever(repository.save(any<ShortenUrlDao>())).thenAnswer { it.arguments[0] }

            val result = urlService.createShortenUrl("https://dkb.com")

            assertThat(result.alias).isEqualTo("abc123")
            assertThat(result.originalUrl).isEqualTo("https://dkb.com")
        }

        @Test
        @DisplayName("Given alias collision When createShortenUrl Then should retry alias generation until unique")
        fun givenAliasCollision_whenCreateShortenUrl_thenRetryAliasGeneration() {
            whenever(repository.findByOriginalUrl("https://dkb.com")).thenReturn(null)
            whenever(baseEncoder.encode(any<String>()))
                .thenReturn("collision") // first attempt collides
                .thenReturn("unique")    // second attempt unique
            whenever(repository.findByAlias("collision")).thenReturn(
                ShortenUrlDao(alias="collision", originalUrl="other.com")
            )
            whenever(repository.findByAlias("unique")).thenReturn(null)
            whenever(repository.save(any<ShortenUrlDao>())).thenAnswer { it.arguments[0] }

            val result = urlService.createShortenUrl("https://dkb.com")

            assertThat(result.alias).isEqualTo("unique")
        }
    }

    @Nested
    @DisplayName("getOriginalUrlByAlias")
    inner class GetOriginalUrlByAliasTest {

        @Test
        @DisplayName("Given an alias When getOriginalUrlByAlias Then should return the corresponding DAO")
        fun givenAlias_whenGetOriginalUrlByAlias_thenReturnDao() {
            val dao = ShortenUrlDao(alias = "abc123", originalUrl = "https://dkb.com")
            whenever(repository.findByAlias("abc123")).thenReturn(dao)

            val result = urlService.getOriginalUrlByAlias("abc123")

            assertThat(result).isEqualTo(dao)
        }

        @Test
        @DisplayName("Given a non-existent alias When getOriginalUrlByAlias Then should throw AliasNotFoundException")
        fun givenNonExistentAlias_whenGetOriginalUrlByAlias_thenThrowNotFound() {
            whenever(repository.findByAlias("unknown")).thenReturn(null)

            val exception = org.junit.jupiter.api.assertThrows<AliasNotFoundException> {
                urlService.getOriginalUrlByAlias("unknown")
            }

            assertThat(exception.message).isEqualTo("Alias 'unknown' not found")
        }
    }
}
