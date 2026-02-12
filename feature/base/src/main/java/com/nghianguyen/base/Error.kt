package com.nghianguyen.base

import com.nghianguyen.domain.drinks.R
import com.nghianguyen.drinks.model.Error
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.model.ValidationError
import com.nghianguyen.text.UiText
import com.nghianguyen.text.UiText.*

fun Error.toUiText(): UiText {
    return when (this) {
        is ValidationError -> {
            when (this) {
                ValidationError.INVALID_INPUT -> StringRes(R.string.local_data_error_invalid_input)
            }
        }
        is LocalDataError -> {
            when (this) {
                LocalDataError.ALREADY_EXISTS -> StringRes(R.string.local_data_error_already_exists)
                LocalDataError.DATABASE_ERROR -> StringRes(R.string.error_try_again)
                LocalDataError.UNKNOWN -> StringRes(R.string.error_try_again) // TODO Remove UNKNOWN
            }
        }
        else -> Plain("Unknown error")
    }
}