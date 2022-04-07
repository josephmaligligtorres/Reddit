package com.joseph.myapp.ui.common

import android.annotation.SuppressLint
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebView(
    modifier: Modifier = Modifier,
    url: String
) {
    AndroidView(
        factory = {
            android.webkit.WebView(it).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                loadUrl(url)
            }
        },
        modifier = modifier
    )
}