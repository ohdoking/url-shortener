package com.dkb.urlshortener.api.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class CreateUrlRequest(
    @field:NotBlank(message = "Original URL must not be blank")
    @field:Size(max = 2000, message = "Original URL is too long")
    @field:Pattern(
        regexp = "^(https?|ftp)://[\\w.-]+(:\\d+)?(/.*)?$",
        message = "Invalid URL format"
    )
    val originalUrl: String
)