package com.dkb.urlshortener

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class UrlshortenerApplication

fun main(args: Array<String>) {
	runApplication<UrlshortenerApplication>(*args)
}
