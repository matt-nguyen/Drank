package com.nghianguyen.consume.viewmodel.wine

import com.nghianguyen.consume.viewmodel.ConsumeDrinkEvent

sealed interface ConsumeWineEvent : ConsumeDrinkEvent {
    object AddWineBrandSuccess : ConsumeWineEvent
    data class AddWineBrandError(val errorMsg: String) : ConsumeWineEvent
    object AddWineSuccess : ConsumeWineEvent
    data class AddWineError(val errorMsg: String) : ConsumeWineEvent
    object SubmitConsumedWineSuccess : ConsumeWineEvent
    data class SubmitConsumedWineError(val errorMsg: String) : ConsumeWineEvent
}