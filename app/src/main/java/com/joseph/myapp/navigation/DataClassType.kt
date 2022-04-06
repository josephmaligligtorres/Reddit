package com.joseph.myapp.navigation

import android.os.Bundle
import androidx.navigation.NavType

class DataClassType : NavType<String>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): String? {
        return bundle.getString(key)
    }

    override fun parseValue(value: String): String {
        return value
    }

    override fun put(bundle: Bundle, key: String, value: String) {
        bundle.putString(key, value)
    }
}