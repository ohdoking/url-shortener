package com.dkb.urlshortener.api.controller

import com.dkb.urlshortener.api.dto.CreateUrlRequest
import com.dkb.urlshortener.core.exception.AliasNotFoundException
import com.dkb.urlshortener.core.model.ShortenUrlDao
import com.dkb.urlshortener.core.service.UrlService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [UrlApiController::class])
@DisplayName("UrlApiController WebMvcTest")
class UrlControllerWebMvcTest(@Autowired val mockMvc: MockMvc) {

    @MockitoBean
    private lateinit var urlService: UrlService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Nested
    @DisplayName("Create Short URL")
    inner class CreateShortUrlTest {

        @Test
        @DisplayName("Given valid original URL When createShortUrl Then return ShortUrlResponse")
        fun givenValidOriginalUrl_whenCreateShortUrl_thenReturnShortUrlResponse() {
            val originalUrl = "https://dkb.com"
            val dao = ShortenUrlDao(alias = "abc123", originalUrl = originalUrl)
            whenever(urlService.createShortenUrl(originalUrl)).thenReturn(dao)

            val requestDto = CreateUrlRequest(originalUrl = originalUrl)

            mockMvc.perform(
                post("/api/v1/urls")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto))
            )
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.alias").value("abc123"))
                .andExpect(jsonPath("$.originalUrl").value(originalUrl))
                .andExpect(jsonPath("$.shortUrl").value("http://localhost/abc123"))
        }

        @Test
        @DisplayName("Given invalid URL format When createShortUrl Then return HTTP 400 with validation error")
        fun givenInvalidUrl_whenCreateShortUrl_thenReturnBadRequest() {
            val requestDto = CreateUrlRequest(originalUrl = "ht!tp://invalid-url")

            mockMvc.perform(
                post("/api/v1/urls")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto))
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.originalUrl").value("Invalid URL format"))
        }

        //@TODO need to solve the issue.
        @Test
        @DisplayName("Given empty URL format When createShortUrl Then return HTTP 400 with validation error")
        fun givenEmptyUrl_whenCreateShortUrl_thenReturnBadRequest() {
            val requestDto = CreateUrlRequest(originalUrl = " ")

            mockMvc.perform(
                post("/api/v1/urls")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto))
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.originalUrl").value("Original URL must not be blank"))
        }

        @Test
        @DisplayName("Given too long URL When createShortUrl Then return HTTP 400 with validation error")
        fun givenTooLongUrl_whenCreateShortUrl_thenReturnBadRequest() {
            val longUrl = "https://dkb.com/" + "a".repeat(5000)
            val requestDto = CreateUrlRequest(originalUrl = longUrl)

            mockMvc.perform(
                post("/api/v1/urls")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto))
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.originalUrl").value("Original URL is too long"))
        }
    }

    @Nested
    @DisplayName("Get Original URL")
    inner class GetOriginalUrlTest {

        @Test
        @DisplayName("Given existing alias When getOriginalUrl Then return ShortUrlResponse")
        fun givenExistingAlias_whenGetOriginalUrl_thenReturnShortUrlResponse() {
            val alias = "abc123"
            val originalUrl = "https://dkb.com"
            val dao = ShortenUrlDao(alias = alias, originalUrl = originalUrl)
            whenever(urlService.getOriginalUrlByAlias(alias)).thenReturn(dao)

            mockMvc.perform(get("/api/v1/urls/{alias}", alias))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.alias").value(alias))
                .andExpect(jsonPath("$.originalUrl").value(originalUrl))
                .andExpect(jsonPath("$.shortUrl").value("http://localhost/abc123"))
        }

        @Test
        @DisplayName("Given non-existing alias When getOriginalUrl Then return HTTP 404")
        fun givenNonExistingAlias_whenGetOriginalUrl_thenReturnNotFound() {
            val alias = "nonexist"
            whenever(urlService.getOriginalUrlByAlias(alias)).thenThrow(AliasNotFoundException(alias))

            mockMvc.perform(get("/api/v1/urls/{alias}", alias))
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$").value("Alias '$alias' not found"))
        }
    }
}
