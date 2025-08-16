package com.dkb.urlshortener.api.controller

import com.dkb.urlshortener.core.exception.AliasNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(AliasNotFoundException::class)
    fun handleNotFound(ex: AliasNotFoundException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleBadRequest(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String?>> {
        val errors: Map<String, String?> = ex.bindingResult.fieldErrors
            .associate { fieldError -> fieldError.field to fieldError.defaultMessage }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }
}