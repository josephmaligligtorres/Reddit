package com.joseph.myapp.helper

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    private var isInitialized = false

    fun setup(action: () -> Unit) {
        if (!isInitialized) {
            isInitialized = true
            action()
        }
    }
}