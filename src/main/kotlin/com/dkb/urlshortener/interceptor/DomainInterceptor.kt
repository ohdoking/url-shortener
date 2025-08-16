package com.dkb.urlshortener.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class DomainInterceptor: HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        // http or https
        val scheme = request.scheme
        // your server host, e.g., localhost or short.dkb.com
        val host = request.serverName
        // usually 80 or 443
        val port = request.serverPort
        val domain = if ((scheme == "http" && port == 80) || (scheme == "https" && port == 443)) {
            "$scheme://$host"
        } else {
            "$scheme://$host:$port"
        }
        request.setAttribute("currentDomain", domain)
        return true
    }
}