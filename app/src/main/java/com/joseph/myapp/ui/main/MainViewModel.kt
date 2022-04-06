package com.joseph.myapp.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseph.myapp.helper.ResponseResult
import com.joseph.myapp.navigation.NavDirection
import com.joseph.myapp.use_case.GetSubredditsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class MainUiState(
    val isLoadingSubreddits: Boolean = false,
    val errorMessage: String = "",
    val errorTrigger: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getSubredditsUseCase: GetSubredditsUseCase
) : ViewModel() {
    private lateinit var navDirection: NavDirection
    private val viewModelState = MutableStateFlow(MainUiState())
    val uiState = viewModelState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        viewModelState.value
    )

    private fun getState(): MainUiState {
        return viewModelState.value
    }

    val onSetup: (navDirection: NavDirection) -> Unit = { navDirection ->
        this.navDirection = navDirection
    }

    val onLoadSubreddits: () -> Unit = {
        viewModelScope.launch {
            viewModelState.update {
                it.copy(
                    isLoadingSubreddits = true
                )
            }

            when (val result = withContext(Dispatchers.IO) { getSubredditsUseCase() }) {
                is ResponseResult.Success -> {
                    for (i in result.data.data.children) {
                        Log.e("puke", "${i.data.title} - ${i.data.name}")
                    }
                }
                is ResponseResult.Error -> {
                    viewModelState.update {
                        it.copy(
                            errorMessage = result.message,
                            errorTrigger = !it.errorTrigger
                        )
                    }
                }
            }

            viewModelState.update {
                it.copy(
                    isLoadingSubreddits = false
                )
            }
        }
    }
}