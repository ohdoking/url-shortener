package com.dkb.urlshortener.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals
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
        @DisplayName("Given a string When encode Then return non-empty string")
        fun givenString_whenEncode_thenReturnNonEmptyString() {
            val input = "HelloWorld123"
            val result = encoder.encode(input)
            assertTrue(result.isNotEmpty(), "Encoded string should not be empty")
        }
    }

    @Nested
    @DisplayName("Decode Functionality")
    inner class DecodeTests {

        @Test
        @DisplayName("Given an encoded string When decode Then return original string")
        fun givenEncodedString_whenDecode_thenReturnOriginal() {
            val original = "HelloWorld123"
            val encoded = encoder.encode(original)
            val decoded = encoder.decode(encoded)
            assertEquals(original, decoded, "Decoded string should match original")
        }

        @Test
        @DisplayName("Given invalid encoded string When decode Then throw exception or handle gracefully")
        fun givenInvalidEncodedString_whenDecode_thenHandle() {
            val invalid = "!!invalid$$"
            Assertions.assertThrows(Exception::class.java) {
                encoder.decode(invalid)
            }
        }
    }

    @Nested
    @DisplayName("Encode and Decode Reversibility")
    inner class ReversibilityTests {

        @Test
        @DisplayName("Given any string When encode and then decode Then return original")
        fun givenAnyString_whenEncodeDecode_thenReturnOriginal() {
            val original = "RandomString987"
            val encoded = encoder.encode(original)
            val decoded = encoder.decode(encoded)
            assertEquals(original, decoded, "Encoding and then decoding should return original string")
        }
    }
}

