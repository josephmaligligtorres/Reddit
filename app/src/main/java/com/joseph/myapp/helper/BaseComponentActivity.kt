package com.joseph.myapp.helper

import android.os.Bundle
import androidx.activity.ComponentActivity
import timber.log.Timber

abstract class BaseComponentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("${javaClass.simpleName} onCreate")
    }

    override fun onStart() {
        super.onStart()
        Timber.e("${javaClass.simpleName} onStart")
    }

    override fun onResume() {
        super.onResume()
        Timber.e("${javaClass.simpleName} onResume")
    }

    override fun onPause() {
        super.onPause()
        Timber.e("${javaClass.simpleName} onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("${javaClass.simpleName} onDestroy")
    }
}
