package com.dkb.urlshortener.core.repository

import com.dkb.urlshortener.core.model.ShortenUrlDao
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@DataJpaTest
@ActiveProfiles("test")
class ShortenUrlDaoTest @Autowired constructor(
    val repository: ShortenUrlRepository
) {

    @Nested
    @DisplayName("Save and Retrieve ShortenUrlDao")
    inner class SaveAndRetrieve {

        @Test
        @DisplayName("Given a valid ShortenUrlDao When saved Then should retrieve by alias successfully")
        fun givenValidDao_whenSave_thenRetrieveByAlias() {
            val dao = ShortenUrlDao(alias = "abc123", originalUrl = "https://dkb.com")
            repository.save(dao)

            val found = repository.findByAlias("abc123")
            assertThat(found).isNotNull
            assertThat(found?.originalUrl).isEqualTo("https://dkb.com")
            assertThat(found?.id).isNotNull
            assertThat(found?.createdAt).isBeforeOrEqualTo(LocalDateTime.now())
        }

        @Test
        @DisplayName("Given a valid ShortenUrlDao When saved Then should retrieve by original url successfully")
        fun givenValidDao_whenSave_thenRetrieveByOriginalUrl() {
            val dao = ShortenUrlDao(alias = "abc123", originalUrl = "https://dkb.com")
            repository.save(dao)

            val found = repository.findByOriginalUrl("https://dkb.com")
            assertThat(found).isNotNull
            assertThat(found?.originalUrl).isEqualTo("https://dkb.com")
            assertThat(found?.id).isNotNull
            assertThat(found?.createdAt).isBeforeOrEqualTo(LocalDateTime.now())
        }


    }

    @Nested
    @DisplayName("Alias uniqueness constraint")
    inner class AliasConstraint {

        @Test
        @DisplayName("Given duplicate alias When saving Then should throw DataIntegrityViolationException")
        fun givenDuplicateAlias_whenSave_thenThrowException() {
            val dao1 = ShortenUrlDao(alias = "abc123", originalUrl = "https://dkb.com")
            val dao2 = ShortenUrlDao(alias = "abc123", originalUrl = "https://dkb2.com")
            repository.save(dao1)

            val exception = org.junit.jupiter.api.assertThrows<DataIntegrityViolationException> {
                repository.saveAndFlush(dao2)
            }

            assertThat(exception.message).contains("alias")
        }
    }
}
