package com.dkb.urlshortener.util

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.DisplayName
import kotlin.test.Test
import kotlin.test.assertTrue

@DisplayName("Base62Encoder Unit Tests")
class Base62EncoderTest {

    private lateinit var encoder: Base62Encoder

    @BeforeEach
    fun setUp() {
        encoder = Base62Encoder()
    }

    @Nested
    @DisplayName("Encode Functionality")
    inner class EncodeTests {

        @Test
        @DisplayName("Given a string When encode Then return non Empty and less than 8 character")
        fun givenString_whenEncode_thenReturnNonEmptyAnd7Character() {
            val input = "HelloWorld123"
            val result = encoder.encode(input)
            assertTrue(result.isNotEmpty(), "Encoded string should not be empty")
            assertTrue(result.length < 8, "The result should be less than 8 character")
        }

        @Test
        @DisplayName("Given a long input string When encode Then return non Empty and less than 8 character")
        fun givenLongInputString_whenEncode_thenReturnNonEmptyAnd7Character() {
            val input = "HelloWorld111111111111111111111111111"
            val result = encoder.encode(input)
            assertTrue(result.isNotEmpty(), "Encoded string should not be empty")
            assertTrue(result.length < 8, "The result should be less than 8 character")
        }

        @Test
        @DisplayName("Given a short input string When encode Then return non Empty and less than 8 character")
        fun givenShortInputString_whenEncode_thenReturnNonEmptyAnd7Character() {
            val input = "H"
            val result = encoder.encode(input)
            assertTrue(result.isNotEmpty(), "Encoded string should not be empty")
            assertTrue(result.length < 8, "The result should be less than 8 character")
        }
    }
}

