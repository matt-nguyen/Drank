package com.nghianguyen.consume.viewmodel.beer

import com.nghianguyen.consume.viewmodel.ConsumeDrinkEvent

sealed interface ConsumeBeerEvent : ConsumeDrinkEvent {
    object AddBeerBrandSuccess : ConsumeBeerEvent
    data class AddBeerBrandError(val errorMsg: String) : ConsumeBeerEvent
    object AddBeerSuccess : ConsumeBeerEvent
    data class AddBeerError(val errorMsg: String) : ConsumeBeerEvent
    object SubmitConsumedBeerSuccess : ConsumeBeerEvent
    data class SubmitConsumedBeerError(val errorMsg: String) : ConsumeBeerEvent
}
