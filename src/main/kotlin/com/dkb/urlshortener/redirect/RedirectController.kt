package com.dkb.urlshortener.redirect

import com.dkb.urlshortener.core.service.UrlService
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class RedirectController(
    private val urlService: UrlService
) {

    private val logger: Logger = LoggerFactory.getLogger(RedirectController::class.java)

    @GetMapping("/{alias}")
    fun redirect(@PathVariable alias: String, response: HttpServletResponse) {
        logger.info("Received redirect request for alias: {}", alias)

        val dao = urlService.getOriginalUrlByAlias(alias)

        // Use 301 Moved Permanently
        response.status = HttpStatus.MOVED_PERMANENTLY.value()
        response.setHeader("Location", dao.originalUrl)

        logger.info("Redirecting alias {} to URL {}", alias, dao.originalUrl)

    }
}