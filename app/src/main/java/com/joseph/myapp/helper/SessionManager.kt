package com.joseph.myapp.helper

import android.content.Context
import android.content.SharedPreferences
import com.securepreferences.SecurePreferences

object SessionManager {
    private lateinit var sessionManager: SecurePreferences

    fun init(context: Context) {
        sessionManager = SecurePreferences(context)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    private const val KEY_FIRST_INSTALL = "SessionManager.KEY_FIRST_INSTALL"
    private const val KEY_BEARER_TOKEN = "SessionManager.KEY_BEARER_TOKEN"

    var firstInstall: Boolean
        get() = sessionManager.getBoolean(KEY_FIRST_INSTALL, true)
        set(value) {
            sessionManager.edit { it.putBoolean(KEY_FIRST_INSTALL, value) }
        }

    var bearerToken: String
        get() = sessionManager.getString(KEY_BEARER_TOKEN, "") ?: ""
        set(value) {
            sessionManager.edit { it.putString(KEY_BEARER_TOKEN, value.trim()) }
        }
}
