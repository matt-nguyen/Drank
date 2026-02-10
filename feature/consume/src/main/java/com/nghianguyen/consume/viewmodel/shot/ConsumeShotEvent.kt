package com.nghianguyen.consume.viewmodel.shot

import com.nghianguyen.consume.viewmodel.ConsumeDrinkEvent

sealed interface ConsumeShotEvent: ConsumeDrinkEvent {
    object SubmitConsumeShotSuccess: ConsumeShotEvent
}
