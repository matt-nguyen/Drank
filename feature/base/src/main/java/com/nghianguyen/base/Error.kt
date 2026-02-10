package com.nghianguyen.base

import com.nghianguyen.domain.drinks.R
import com.nghianguyen.drinks.model.Error
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.text.UiText

fun Error.toUiText(): UiText {
    return when (this) {
        is LocalDataError -> {
            when (this) {
                LocalDataError.ALREADY_EXISTS -> UiText.StringRes(R.string.local_data_error_already_exists)
                LocalDataError.INVALID_INPUT -> UiText.StringRes(R.string.local_data_error_invalid_input)
                else -> UiText.StringRes(R.string.error_try_again)
            }
        }
        else -> UiText.Plain("Unknown error")
    }
}