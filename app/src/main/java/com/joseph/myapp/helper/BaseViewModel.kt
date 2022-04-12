package com.joseph.myapp.helper

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    private var isInitialized = false

    fun setup(onSetupEvent: () -> Unit) {
        if (!isInitialized) {
            isInitialized = true
            onSetupEvent()
        }
    }
}