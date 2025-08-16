package com.dkb.urlshortener.core.exception

class AliasNotFoundException(alias: String) :
    RuntimeException("Alias '$alias' not found")