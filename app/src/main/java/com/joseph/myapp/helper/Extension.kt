package com.joseph.myapp.helper

import android.content.Context
import androidx.navigation.NavBackStackEntry
import java.lang.ref.WeakReference

fun WeakReference<Context>.getString(resId: Int): String {
    return this.get()?.getString(resId) ?: ""
}

inline fun <reified T> NavBackStackEntry.safeArgsDataClass(key: String): T? {
    val jsonString = arguments?.getString(key, "")?.trim() ?: ""
    return dataClassDecoder<T>(jsonString)
}

fun Any?.isNull(): Boolean {
    return this == null
}

fun Any?.isNotNull(): Boolean {
    return this != null
}
