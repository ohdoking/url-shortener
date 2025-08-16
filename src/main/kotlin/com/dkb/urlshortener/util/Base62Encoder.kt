package com.dkb.urlshortener.util

import org.springframework.stereotype.Component

import java.math.BigInteger

/**
 * Encodes a string into a fixed-length Base62 representation.
 *
 * Implementation is based on the reference from:
 * https://mojoauth.com/binary-encoding-decoding/base62-with-kotlin/
 *
 */
@Component
class Base62Encoder : BaseEncoder {

    private val ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    private val BASE = BigInteger.valueOf(ALPHABET.length.toLong())
    private val SHORT_URL_LENGTH = 7

    override fun encode(characters: String): String {
        val number = BigInteger(characters.toByteArray()).abs()

        if (number == BigInteger.ZERO) return ALPHABET[0].toString().repeat(SHORT_URL_LENGTH)

        val sb = StringBuilder()
        var n = number
        while (n > BigInteger.ZERO) {
            val remainder = n.mod(BASE).toInt()
            sb.append(ALPHABET[remainder])
            n = n.divide(BASE)
        }

        val base62 = sb.reverse().toString()
        return if (base62.length <= SHORT_URL_LENGTH) base62 else base62.takeLast(SHORT_URL_LENGTH)
    }
}