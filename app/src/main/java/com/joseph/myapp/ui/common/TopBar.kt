package com.joseph.myapp.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joseph.myapp.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TopBar(
    searchInput: String,
    onSearchInputChanged: (String) -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFCEE3F8)),
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(
            modifier = Modifier
                .height(10.dp)
        )

        Image(
            modifier = Modifier
                .padding(start = 18.dp)
                .height(30.dp),
            painter = painterResource(id = R.drawable.ic_splash_logo),
            contentDescription = null
        )

        Spacer(
            modifier = Modifier
                .height(10.dp)
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(horizontal = 18.dp),
            shape = RoundedCornerShape(6.dp),
            elevation = 3.dp
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                value = searchInput,
                onValueChange = {
                    onSearchInputChanged(it)
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFFFFFFF),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search_hint),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFFC3C3C3)
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                },
                trailingIcon = {
                    if (searchInput.isNotEmpty()) {
                        Icon(
                            modifier = Modifier
                                .clickable {
                                    onSearchInputChanged("")
                                    keyboard?.hide()
                                    focusManager.clearFocus()
                                },
                            painter = painterResource(id = R.drawable.ic_search_close),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }
                },
                readOnly = false,
                textStyle = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF336699)
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboard?.hide()
                        focusManager.clearFocus()
                    }
                )
            )
        }

        Spacer(
            modifier = Modifier
                .height(10.dp)
        )
    }
}