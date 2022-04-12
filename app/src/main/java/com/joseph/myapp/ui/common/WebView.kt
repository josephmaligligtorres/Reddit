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

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebView(
    modifier: Modifier = Modifier,
    url: String,
    onStoreHandler: (SslErrorHandler?, String) -> Unit
) {
    var backEnabled by rememberSaveable { mutableStateOf(false) }
    var webView: WebView? = null

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
                        backEnabled = view.canGoBack()
                    }

                    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                        val errorTitle = when (error?.primaryError) {
                            SslError.SSL_NOTYETVALID -> it.getString(R.string.certificate_not_yet_valid_occurred)
                            SslError.SSL_EXPIRED -> it.getString(R.string.certificate_expired_occurred)
                            SslError.SSL_IDMISMATCH -> it.getString(R.string.hostname_mismatch_occurred)
                            SslError.SSL_UNTRUSTED -> it.getString(R.string.untrusted_certificate_occurred)
                            SslError.SSL_DATE_INVALID -> it.getString(R.string.invalid_certificate_date_occurred)
                            else -> it.getString(R.string.something_went_wrong)
                        }

                        onStoreHandler(handler, errorTitle)
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

    BackHandler(enabled = backEnabled) {
        webView?.goBack()
    }
}

@Composable
fun SslErrorDialog(
    handler: SslErrorHandler?,
    errorTitle: String,
    onCloseDialog: () -> Unit,
    onBack: () -> Unit
) {
    if (handler.isNotNull()) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = errorTitle,
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