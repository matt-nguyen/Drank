package com.nghianguyen.consume.viewmodel.wine

import com.nghianguyen.consume.viewmodel.ConsumeDrinkEvent

sealed interface ConsumeWineEvent : ConsumeDrinkEvent {
    object SubmitConsumedWineSuccess : ConsumeWineEvent
}