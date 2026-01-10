package com.nghianguyen.drinks.model

import androidx.annotation.StringRes
import com.nghianguyen.domain.drinks.R

sealed interface Error {
    object UNKNOWN: Error
}

enum class LocalDataError(@StringRes val stringRes: Int): Error {
    ALREADY_EXISTS(R.string.local_data_error_already_exists),
    DATABASE_ERROR(R.string.local_data_error_database_error),
    INVALID_INPUT(R.string.local_data_error_invalid_input),
    UNKNOWN(R.string.local_data_error_unknown)
}
