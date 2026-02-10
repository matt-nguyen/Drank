package com.nghianguyen.local.ext

import android.database.SQLException
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.asOk
import com.nghianguyen.drinks.model.LocalDataError
import kotlinx.coroutines.CancellationException


fun <V> Result<V, Throwable>.mapLocalDataError(): Result<V, LocalDataError> {
    return when {
        isOk -> this.asOk()
        else -> {
            Log.e("mapLocalDataError", "Exception captured during operation", error)
            if (error is CancellationException)
                throw error

            val dataError =
                when (error) {
                    is SQLiteConstraintException -> LocalDataError.ALREADY_EXISTS
                    is SQLException -> LocalDataError.DATABASE_ERROR
                    is IllegalArgumentException -> LocalDataError.INVALID_INPUT
                    else -> LocalDataError.UNKNOWN // TODO should we return Error.UNKNOWN here?
                }
            Err(dataError)
        }
    }
}
