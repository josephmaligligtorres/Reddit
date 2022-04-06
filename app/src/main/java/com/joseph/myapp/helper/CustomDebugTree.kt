package com.joseph.myapp.helper

import timber.log.Timber

class CustomDebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String {
        return "$GLOBAL_TAG (${element.fileName}:${element.lineNumber})"
    }
}
