package com.cookiedinner.composetest.ViewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*

class MainActivityViewModel: ViewModel() {
    var darkTheme: MutableState<Boolean> = mutableStateOf(false)

    fun changeTheme() {
        darkTheme.value = !darkTheme.value
    }
}