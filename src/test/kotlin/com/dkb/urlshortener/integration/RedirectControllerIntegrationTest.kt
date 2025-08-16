package com.dkb.urlshortener.integration

import com.dkb.urlshortener.api.dto.CreateUrlRequest
import com.dkb.urlshortener.core.model.ShortenUrlDao
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("RedirectController Integration Test")
class RedirectControllerIntegrationTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper
) {

    @Nested
    @DisplayName("Redirect shorten URL")
    inner class RedirectShortenUrlTest {
        @Test
        @DisplayName("Given created alias When GET /{alias} Then respond with 301 redirect to original URL")
        fun givenCreatedAlias_whenRedirect_then301Redirect() {
            val originalUrl = "https://dkb.com/page"

            val request = CreateUrlRequest(originalUrl)
            val mvcResult = mockMvc.perform(
                post("/api/v1/urls")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isCreated)
                .andReturn()

            val dao = objectMapper.readValue(mvcResult.response.contentAsString, ShortenUrlDao::class.java)

            mockMvc.perform(get("/{alias}", dao.alias))
                .andExpect(status().isMovedPermanently)
                .andExpect(redirectedUrl(originalUrl))
        }

        @Test
        @DisplayName("Given non-existing alias When GET /{alias} Then return HTTP 404")
        fun givenNonExistingAlias_whenRedirect_thenNotFound() {
            val alias = "nonexist123"

            mockMvc.perform(get("/{alias}", alias))
                .andExpect(status().isNotFound)
        }
    }
}
