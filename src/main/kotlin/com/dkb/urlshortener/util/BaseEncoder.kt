package com.dkb.urlshortener.util

interface BaseEncoder {
    fun encode(characters: String): String
}
