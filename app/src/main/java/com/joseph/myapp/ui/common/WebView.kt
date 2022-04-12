package com.joseph.myapp.ui.common

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.joseph.myapp.R
import com.joseph.myapp.helper.isNotNull
import com.joseph.myapp.helper.isNull

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebView(
    modifier: Modifier = Modifier,
    url: String,
    onBack: () -> Unit
) {
    var backSwitch by rememberSaveable { mutableStateOf(false) }
    var title by rememberSaveable { mutableStateOf("") }
    var handler: SslErrorHandler? by remember { mutableStateOf(null) }
    var webView: WebView? by remember { mutableStateOf(null) }

    if (handler.isNull() && title.isNotEmpty()) {
        onBack()
    }

    AndroidView(
        modifier = modifier,
        factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                        backSwitch = view.canGoBack()
                    }

                    override fun onReceivedSslError(view: WebView?, _handler: SslErrorHandler?, error: SslError?) {
                        title = when (error?.primaryError) {
                            SslError.SSL_NOTYETVALID -> it.getString(R.string.ssl_not_yet_valid_title)
                            SslError.SSL_EXPIRED -> it.getString(R.string.ssl_expired_title)
                            SslError.SSL_IDMISMATCH -> it.getString(R.string.ssl_id_mismatch_title)
                            SslError.SSL_UNTRUSTED -> it.getString(R.string.ssl_untrusted_title)
                            SslError.SSL_DATE_INVALID -> it.getString(R.string.ssl_date_invalid_title)
                            else -> it.getString(R.string.something_went_wrong)
                        }

                        handler = _handler
                    }
                }

                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true

                loadUrl(url)
                webView = this
            }
        },
        update = {
            webView = it
        }
    )

    BackHandler(enabled = backSwitch) {
        webView?.goBack()
    }

    SslErrorDialog(
        handler = handler,
        title = title,
        onCloseDialog = {
            handler = null
            title = ""
        },
        onBack = onBack
    )
}

@Composable
fun SslErrorDialog(
    handler: SslErrorHandler?,
    title: String,
    onCloseDialog: () -> Unit,
    onBack: () -> Unit
) {
    if (handler.isNotNull()) {
        AlertDialog(
            backgroundColor = Color(0xFFFFFFFF),
            onDismissRequest = {},
            title = {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = Color(0xFF000000)
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.ssl_error_content),
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    color = Color(0xFF000000)
                )
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp, end = 30.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        modifier = Modifier
                            .clickable {
                                handler?.proceed()
                                onCloseDialog()
                            },
                        text = stringResource(id = R.string.yes_continue),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Normal,
                        fontSize = 15.sp,
                        color = Color(0xFF336699)
                    )

                    Spacer(
                        modifier = Modifier
                            .width(15.dp)
                    )

                    Text(
                        modifier = Modifier
                            .clickable {
                                onBack()
                                onCloseDialog()
                            },
                        text = stringResource(id = R.string.no_go_back),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Normal,
                        fontSize = 15.sp,
                        color = Color(0xFF336699)
                    )
                }
            }
        )
    }
}