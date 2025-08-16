package com.dkb.urlshortener.util

import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@DisplayName("UrlUtil Unit Tests")
class UrlUtilTest {

    @Test
    @DisplayName("Given request with currentDomain When buildShortUrl Then return full URL")
    fun givenRequestWithCurrentDomain_whenBuildShortUrl_thenReturnFullUrl() {
        val request = mock<HttpServletRequest>()
        whenever(request.getAttribute("currentDomain")).thenReturn("https://example.com")

        val alias = "abc123"
        val result = UrlUtil.buildShortUrl(request, alias)

        assertEquals("https://example.com/abc123", result)
    }

    @Test
    @DisplayName("Given request without currentDomain When buildShortUrl Then return URL with empty domain")
    fun givenRequestWithoutCurrentDomain_whenBuildShortUrl_thenReturnUrlWithEmptyDomain() {
        val request = mock<HttpServletRequest>()
        whenever(request.getAttribute("currentDomain")).thenReturn(null)

        val alias = "abc123"
        val result = UrlUtil.buildShortUrl(request, alias)

        assertEquals("/abc123", result)
    }
}