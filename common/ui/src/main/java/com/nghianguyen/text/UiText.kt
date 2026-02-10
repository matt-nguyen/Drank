package com.nghianguyen.text

import android.content.Context

sealed class UiText {
    data class Plain(val value: String): UiText()
    data class StringRes(val resId: Int): UiText() // TODO fix @StringRes
}

fun UiText.toStringText(context: Context): String {
    return when (this) {
        is UiText.Plain -> value
        is UiText.StringRes -> context.getString(resId)
    }
}