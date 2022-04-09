package com.joseph.myapp.helper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope

@Composable
fun InitEffect(block: suspend CoroutineScope.() -> Unit) {
    var state by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        if (!state) {
            state = true
            block()
        }
    }
}

@Composable
fun TriggeredEffect(uiState: Any?, trigger: Boolean, block: suspend CoroutineScope.() -> Unit) {
    var state: Boolean? by rememberSaveable { mutableStateOf(null) }
    val oldState = state

    LaunchedEffect(key1 = uiState) {
        if (uiState.isNotNull()) {
            if (trigger != state) {
                state = trigger
                oldState?.let {
                    block()
                }
            }
        } else {
            throw Exception("uiState can't be null!")
        }
    }
}
