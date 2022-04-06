package com.joseph.myapp.helper

import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.joseph.myapp.use_case.RefreshTokenUseCase
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@AndroidEntryPoint
abstract class BaseComponentActivity : ComponentActivity() {
    private var tokenJob: Job? = null

    @Inject
    lateinit var refreshTokenUseCase: RefreshTokenUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("${javaClass.simpleName} onCreate")
        setBearerTokenValidity(0L)
    }

    override fun onStart() {
        super.onStart()
        Timber.e("${javaClass.simpleName} onStart")
    }

    override fun onResume() {
        super.onResume()
        Timber.e("${javaClass.simpleName} onResume")
        refreshBearerTokenScheduler()
    }

    override fun onPause() {
        super.onPause()
        Timber.e("${javaClass.simpleName} onPause")
        clearJob()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("${javaClass.simpleName} onDestroy")
    }

    private fun clearJob() {
        tokenJob?.cancel()
        tokenJob = null
    }

    private fun refreshBearerTokenScheduler() {
        // Refresh will be done 5 minutes before expiry time
        var remainingTime = getBearerTokenValidity()
        remainingTime = if (remainingTime <= 0) 0 else remainingTime

        Timber.e(TIMER_TAG + "Token remaining day(s): ${TimeUnit.MILLISECONDS.toDays(remainingTime)}")
        Timber.e(TIMER_TAG + "Token remaining hour(s): ${TimeUnit.MILLISECONDS.toHours(remainingTime) % 24}")
        Timber.e(TIMER_TAG + "Token remaining minute(s): ${TimeUnit.MILLISECONDS.toMinutes(remainingTime) % 60}")
        Timber.e(TIMER_TAG + "Token remaining second(s): ${TimeUnit.MILLISECONDS.toSeconds(remainingTime) % 60}")

        // Don't invoke anything when token job is not null
        // Safety check
        tokenJob?.let {
            return
        }

        tokenJob = CoroutineScope(Dispatchers.IO).launch {
            delay(remainingTime)
            Timber.e(TIMER_TAG + "Refresh token started!")

            when (val result = withContext(Dispatchers.IO) { refreshTokenUseCase() }) {
                is ResponseResult.Success -> {
                    result.data.apply {
                        val validity = SystemClock.elapsedRealtime() + (expiration * 1000L)
                        setBearerTokenValidity(validity)
                        setBearerToken(accessToken)
                        clearJob()
                        refreshBearerTokenScheduler()
                    }
                }
                is ResponseResult.Error -> {
                    Toast.makeText(this@BaseComponentActivity, result.message, Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }
}
