package com.joseph.myapp.helper

import android.content.Context
import android.content.SharedPreferences
import com.securepreferences.SecurePreferences

object SecuredPreferences {
    private lateinit var sharedPref: SecurePreferences

    fun init(context: Context) {
        sharedPref = SecurePreferences(context)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    private const val KEY_FIRST_INSTALL = "SecuredPreferences.FIRST_INSTALL"
    private const val KEY_BEARER_TOKEN = "SecuredPreferences.BEARER_TOKEN"

    var firstInstall: Boolean
        get() = sharedPref.getBoolean(KEY_FIRST_INSTALL, true)
        set(value) {
            sharedPref.edit { it.putBoolean(KEY_FIRST_INSTALL, value) }
        }

    var bearerToken: String
        get() = sharedPref.getString(KEY_BEARER_TOKEN, "") ?: ""
        set(value) {
            sharedPref.edit { it.putString(KEY_BEARER_TOKEN, value.trim()) }
        }
}
