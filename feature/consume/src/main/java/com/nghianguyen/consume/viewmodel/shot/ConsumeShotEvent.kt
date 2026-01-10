package com.nghianguyen.consume.viewmodel.shot

import com.nghianguyen.consume.viewmodel.ConsumeDrinkEvent

sealed interface ConsumeShotEvent: ConsumeDrinkEvent {
    object AddShotSuccess: ConsumeShotEvent
    data class AddShotError(val errorMsg: String): ConsumeShotEvent
    object SubmitConsumeShotSuccess: ConsumeShotEvent
    data class SubmitConsumeShotError(val errorMsg: String): ConsumeShotEvent
}
