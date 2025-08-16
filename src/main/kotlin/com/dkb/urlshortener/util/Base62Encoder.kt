package com.dkb.urlshortener.util

import org.springframework.stereotype.Component

import java.math.BigInteger

/**
 * Base62 encoder and decoder implementation.
 *
 * Implementation is based on the reference from:
 * https://mojoauth.com/binary-encoding-decoding/base62-with-kotlin/
 */
@Component
class Base62Encoder : BaseEncoder {

    private val ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    private val BASE = BigInteger.valueOf(ALPHABET.length.toLong())

    override fun encode(characters: String): String {
        val number = BigInteger(characters.toByteArray())
        if (number == BigInteger.ZERO) return ALPHABET[0].toString()

        val sb = StringBuilder()
        var n = number
        while (n > BigInteger.ZERO) {
            val remainder = n.mod(BASE).toInt()
            sb.append(ALPHABET[remainder])
            n = n.divide(BASE)
        }
        return sb.reverse().toString()
    }

    override fun decode(characters: String): String {
        var num = BigInteger.ZERO
        for (ch in characters) {
            val index = ALPHABET.indexOf(ch)
            if (index == -1) throw IllegalArgumentException("Invalid Base62 character: $ch")
            num = num.multiply(BASE).add(BigInteger.valueOf(index.toLong()))
        }
        return String(num.toByteArray())
    }
}