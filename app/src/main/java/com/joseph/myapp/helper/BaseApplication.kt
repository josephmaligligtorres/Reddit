package com.joseph.myapp.helper

import android.widget.Toast
import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import com.joseph.myapp.BuildConfig
import com.osama.firecrasher.CrashListener
import com.osama.firecrasher.FireCrasher
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BaseApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(CustomDebugTree())
            Stetho.initializeWithDefaults(this)
            FireCrasher.install(
                this,
                object : CrashListener() {
                    override fun onCrash(throwable: Throwable) {
                        evaluate { activity, _ ->
                            activity?.let {
                                Toast.makeText(it, throwable.stackTraceToString(), Toast.LENGTH_LONG).show()

                                val errorBuilder = StringBuilder()
                                errorBuilder.append(" \n_____________________________START OF CRASH_____________________________\n\n")
                                errorBuilder.append(throwable.stackTraceToString())
                                errorBuilder.append("\n______________________________END OF CRASH______________________________\n\n")
                                Timber.tag(GLOBAL_TAG).e(errorBuilder.toString())
                            }
                        }
                    }
                }
            )
        }

        SessionManager.init(this)
        firstInstall()
    }
}