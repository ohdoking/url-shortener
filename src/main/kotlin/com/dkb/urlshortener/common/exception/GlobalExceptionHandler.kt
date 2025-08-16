package com.dkb.urlshortener.common.exception

import com.dkb.urlshortener.core.exception.AliasNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(AliasNotFoundException::class)
    fun handleNotFound(ex: AliasNotFoundException): ResponseEntity<String> {
        logger.warn("Alias not found: {}", ex.message)
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleBadRequest(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String?>> {
        val errors: Map<String, String?> = ex.bindingResult.fieldErrors
            .associate { fieldError -> fieldError.field to fieldError.defaultMessage }
        logger.error("Validation failed: {}", errors)
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<String> {
        logger.error("Unhandled exception occurred", ex)
        return ResponseEntity(ex.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}