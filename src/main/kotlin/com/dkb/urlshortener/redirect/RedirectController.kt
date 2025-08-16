package com.dkb.urlshortener.redirect

import com.dkb.urlshortener.core.service.UrlService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class RedirectController(
    private val urlService: UrlService
) {

    @GetMapping("/{alias}")
    fun redirect(@PathVariable alias: String, response: HttpServletResponse) {
        val dao = urlService.getOriginalUrlByAlias(alias)
        response.sendRedirect(dao.originalUrl)
    }
}