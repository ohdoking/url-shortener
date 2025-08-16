package com.dkb.urlshortener.integration

import com.dkb.urlshortener.api.dto.CreateUrlRequest
import com.dkb.urlshortener.core.model.ShortenUrlDao
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("UrlApiController Integration Test")
class UrlApiControllerIntegrationTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper
) {

    @Nested
    @DisplayName("Create Short URL")
    inner class CreateShortUrlTest {
        @Test
        @DisplayName("Given valid URL When POST /api/v1/urls Then create and return short URL")
        fun givenValidUrl_whenCreateShortUrl_thenReturnShortUrl() {
            val request = CreateUrlRequest("https://dkb.com")

            mockMvc.perform(
                post("/api/v1/urls")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.alias").exists())
                .andExpect(jsonPath("$.originalUrl").value("https://dkb.com"))
                .andExpect(jsonPath("$.shortUrl").exists())
        }

        @Test
        @DisplayName("Given invalid URL When POST /api/v1/urls Then create and return short URL")
        fun givenInValidUrl_whenCreateShortUrl_thenReturnShortUrl() {
            val request = CreateUrlRequest("wrong://dkb.com")

            mockMvc.perform(
                post("/api/v1/urls")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.originalUrl").value("Invalid URL format"))
        }
    }

    @Nested
    @DisplayName("Get Original URL")
    inner class GetOriginalUrlTest {
        @Test
        @DisplayName("Given created alias When GET /api/v1/urls/{alias} Then return original URL")
        fun givenAlias_whenGetOriginalUrl_thenReturnDao() {
            val request = CreateUrlRequest("https://dkb.com")

            val mvcResult = mockMvc.perform(
                post("/api/v1/urls")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isCreated)
                .andReturn()

            val responseBody = mvcResult.response.contentAsString
            val dao = objectMapper.readValue(responseBody, ShortenUrlDao::class.java)

            mockMvc.perform(get("/api/v1/urls/{alias}", dao.alias))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.alias").value(dao.alias))
                .andExpect(jsonPath("$.originalUrl").value("https://dkb.com"))
        }

        @Test
        @DisplayName("Given non-existing alias When GET /api/v1/urls/{alias} Then return HTTP 404")
        fun givenNonExistingAlias_whenGetOriginalUrl_thenReturnNotFound() {
            val alias = "nonexist"

            mockMvc.perform(get("/api/v1/urls/{alias}", alias))
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$").value("Alias '$alias' not found"))
        }
    }
}
