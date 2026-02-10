package com.nghianguyen.base.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.Result
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel<STATE, ACTION, EVENT>() : ViewModel() {

    protected abstract fun buildInitialState(): STATE
    protected abstract fun onStart()

    abstract fun handleAction(action: ACTION)

    private val _uiState = MutableStateFlow(buildInitialState())

    val uiState = _uiState
        .onStart { onStart() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            _uiState.value
        )

    private val _uiEvent = Channel<EVENT>(Channel.BUFFERED)

    val uiEvent = _uiEvent.receiveAsFlow()
        .shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000)
        )

    protected fun updateState(updater: STATE.() -> STATE) {
        _uiState.update(updater)
    }

    protected fun sendEvent(event: EVENT) {
        launch { _uiEvent.send(event) }
    }

    protected fun <V, E> Result<V, E>.onResult(onSuccess: (V) -> Unit, onFailure: (E) -> Unit) {
        if (isOk) {
            onSuccess(value)
        } else {
            onFailure(error)
        }
    }

    protected fun launch(
        coroutineContext: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(
            context =
                if (coroutineContext[CoroutineExceptionHandler] != null) coroutineContext
                else coroutineContext + defaultCoroutineExceptionHandler,
            block = block
        )
    }

    private val defaultCoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("ViewModel", "Unexpected error", throwable)
    }
}