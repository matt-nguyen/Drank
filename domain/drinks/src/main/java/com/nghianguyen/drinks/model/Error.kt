package com.nghianguyen.drinks.model

sealed interface Error {
    object UNKNOWN: Error
}

enum class ValidationError: Error {
    INVALID_INPUT
}

enum class LocalDataError : Error {
    ALREADY_EXISTS,
    DATABASE_ERROR,
    UNKNOWN
}
