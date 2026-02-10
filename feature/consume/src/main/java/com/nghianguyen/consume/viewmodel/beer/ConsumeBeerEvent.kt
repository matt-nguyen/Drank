package com.nghianguyen.consume.viewmodel.beer

import com.nghianguyen.consume.viewmodel.ConsumeDrinkEvent

sealed interface ConsumeBeerEvent : ConsumeDrinkEvent {
    object SubmitConsumedBeerSuccess : ConsumeBeerEvent
}
