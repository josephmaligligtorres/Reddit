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
    var triggerValue by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        if (!triggerValue) {
            triggerValue = true
            block()
        }
    }
}

@Composable
fun TriggeredEffect(uiState: Any?, trigger: Boolean, block: suspend CoroutineScope.() -> Unit) {
    var triggerValue: Boolean? by rememberSaveable { mutableStateOf(null) }
    val oldValue = triggerValue

    LaunchedEffect(key1 = uiState) {
        if (uiState.isNotNull()) {
            if (trigger != triggerValue) {
                triggerValue = trigger
                oldValue?.let {
                    block()
                }
            }
        } else {
            throw Exception("uiState can't be null!")
        }
    }
}
