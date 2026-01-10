package com.nghianguyen.home.viewmodel.home

sealed class HomeAction {
    object NextDate : HomeAction()
    object PrevDate : HomeAction()
}
