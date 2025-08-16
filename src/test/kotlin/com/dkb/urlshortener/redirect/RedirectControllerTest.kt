package com.dkb.urlshortener.redirect

import com.dkb.urlshortener.core.exception.AliasNotFoundException
import com.dkb.urlshortener.core.model.ShortenUrlDao
import com.dkb.urlshortener.core.service.UrlService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [RedirectController::class])
@DisplayName("RedirectController Tests")
class RedirectControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var urlService: UrlService

    @Nested
    @DisplayName("Redirect shorten URL")
    inner class RedirectShortenUrlTest {
        @Test
        @DisplayName("Given alias When redirect Then respond with redirect to original URL")
        fun givenAlias_whenRedirect_thenRespondWithRedirect() {
            val alias = "abc123"
            val originalUrl = "https://dkb.com/page"

            // Mock service
            val dao = ShortenUrlDao(alias = alias, originalUrl = originalUrl)
            whenever(urlService.getOriginalUrlByAlias(alias)).thenReturn(dao)

            mockMvc.perform(MockMvcRequestBuilders.get("/{alias}", alias))
                .andExpect(status().is3xxRedirection)
                .andExpect(MockMvcResultMatchers.redirectedUrl(originalUrl))
        }

        @Test
        @DisplayName("Given non-existing alias When getOriginalUrl Then return HTTP 404")
        fun givenNonExistingAlias_whenGetOriginalUrl_thenReturnNotFound() {
            val alias = "nonexist"
            whenever(urlService.getOriginalUrlByAlias(alias)).thenThrow(AliasNotFoundException(alias))

            mockMvc.perform(MockMvcRequestBuilders.get("/{alias}", alias))
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$").value("Alias '$alias' not found"))
        }
    }
}
