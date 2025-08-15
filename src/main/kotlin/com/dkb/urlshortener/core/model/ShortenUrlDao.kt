package com.dkb.urlshortener.core.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime
import java.util.UUID


@Entity
@Table(
    name = "shorten_url",
    uniqueConstraints = [UniqueConstraint(columnNames = ["alias"])]
)
data class ShortenUrlDao(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true)
    val alias: String,

    @Column(name = "original_url", nullable = false)
    val originalUrl: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)