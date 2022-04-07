package com.joseph.myapp.helper

import android.content.Context
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.joseph.myapp.BuildConfig
import com.joseph.myapp.R
import com.joseph.myapp.api.AuthApi
import com.joseph.myapp.api.DataApi
import com.joseph.myapp.navigation.CustomNavType
import com.joseph.myapp.navigation.NavData
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.EOFException
import java.lang.ref.WeakReference
import java.lang.reflect.Field
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLPeerUnverifiedException
import okhttp3.Credentials
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

inline fun <reified T> createApi(okHttpClient: OkHttpClient, factory: CallAdapter.Factory, baseUrl: String): T {
    return Retrofit.Builder().run {
        baseUrl(baseUrl)
        addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        addCallAdapterFactory(NetworkResponseAdapterFactory())
        addCallAdapterFactory(factory)
        client(okHttpClient)
        build().create(T::class.java)
    }
}

fun createHttpClient(isAuthApi: Boolean, builder: OkHttpClient.Builder): OkHttpClient {
    val authorization = if (isAuthApi) {
        Credentials.basic(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET)
    } else {
        getBearerToken()
    }

    return builder.run {
        addInterceptor(
            LoggingInterceptor.Builder().run {
                request(GLOBAL_TAG + REQUEST_TAG)
                response(GLOBAL_TAG + RESPONSE_TAG)
                log(Platform.INFO)
                setLevel(Level.BASIC)
                build()
            }
        )
        addInterceptor { chain ->
            val oldRequest = chain.request()
            val newRequest = oldRequest.newBuilder().run {
                addHeader("Connection", "close")
                addHeader("Accept", "application/json")
                addHeader("Content-Type", "application/json")
                addHeader("Authorization", authorization)
                method(oldRequest.method, oldRequest.body)
                build()
            }
            return@addInterceptor chain.proceed(newRequest)
        }

        if (BuildConfig.DEBUG) {
            addNetworkInterceptor(StethoInterceptor())
        }

        build()
    }
}

fun createHttpBuilder(): OkHttpClient.Builder {
    return OkHttpClient.Builder().apply {
        connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        writeTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        readTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        followRedirects(false)
        followSslRedirects(false)
    }
}

fun createAuthenticatorHttpBuilder(): OkHttpClient.Builder {
    return OkHttpClient.Builder().apply {
        connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        writeTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        readTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        authenticator(TokenAuthenticator())
        followRedirects(false)
        followSslRedirects(false)
    }
}

fun createDataApi(): DataApi {
    return createApi(
        okHttpClient = createHttpClient(
            isAuthApi = false,
            builder = createAuthenticatorHttpBuilder()
        ),
        factory = RxJava2CallAdapterFactory.create(),
        baseUrl = BuildConfig.BASE_DATA_API_URL
    )
}

fun createAuthApi(): AuthApi {
    return createApi(
        okHttpClient = createHttpClient(
            isAuthApi = true,
            builder = createHttpBuilder()
        ),
        factory = RxJava2CallAdapterFactory.create(),
        baseUrl = BuildConfig.BASE_AUTH_API_URL
    )
}

fun decodeUnknownError(throwable: Throwable, context: WeakReference<Context>): String {
    return when (throwable) {
        is JsonSyntaxException -> {
            context.getString(R.string.unknown_error_JsonSyntaxException_message)
        }
        else -> {
            context.getString(R.string.unknown_error_else_message)
        }
    }
}

fun decodeNetworkError(throwable: Throwable, context: WeakReference<Context>): String {
    return when (throwable) {
        is EOFException -> {
            context.getString(R.string.network_error_EOFException_message)
        }
        is SocketTimeoutException -> {
            context.getString(R.string.network_error_SocketTimeoutException_message)
        }
        is OutOfMemoryError -> {
            context.getString(R.string.network_error_OutOfMemoryError_message)
        }
        is SSLHandshakeException, is SSLPeerUnverifiedException -> {
            context.getString(R.string.network_error_SSLException_message)
        }
        else -> {
            context.getString(R.string.network_error_else_message)
        }
    }
}

fun getHttpStatus(httpCode: Int): HttpStatus? {
    var httpStatus: HttpStatus? = null
    for (item in HttpStatus.values()) {
        if (httpCode == item.code) {
            httpStatus = item
            break
        }
    }
    return httpStatus
}

fun generateNavData(route: String, vararg safeArgs: Any?, arguments: List<NamedNavArgument> = listOf()): NavData {
    return NavData(
        route = buildString {
            append(route)
            arguments.forEach {
                append("?${it.name}={${it.name}}")
            }
        },
        safeArgs = buildString {
            append(route)
            arguments.forEachIndexed { index, argument ->
                val data = if (index >= safeArgs.size) null else safeArgs[index]
                val dataType = argument.argument.type
                val isNullable = argument.argument.isNullable
                val initialValue = argument.argument.defaultValue

                val newData = if (!isNullable && data.isNull()) {
                    "?${argument.name}=$initialValue"
                } else {
                    val newData = if (dataType == CustomNavType.DataClassType as NavType<*> && data.isNotNull()) {
                        dataClassEncoder(data)
                    } else {
                        data
                    }

                    "?${argument.name}=${newData ?: " "}"
                }

                append(newData)
            }
        },
        arguments = arguments
    )
}

fun generateNavArgument(key: String, dataType: NavType<*>, isNullable: Boolean, initialValue: Any? = null): NamedNavArgument {
    val listOfNonNullable = mutableListOf<NavType<*>>()
    listOfNonNullable.add(NavType.IntType)

    var newDefaultValue = initialValue

    listOfNonNullable.forEach {
        if (it == dataType && isNullable) {
            throw Exception("${it.name} must not be null!")
        }
    }

    if (!isNullable && newDefaultValue.isNull()) {
        throw Exception("Initial value is required if isNullable was set to false!")
    }

    if (dataType == CustomNavType.DataClassType && !isNullable) {
        newDefaultValue = dataClassEncoder(newDefaultValue)
    }

    return navArgument(key) {
        type = dataType
        nullable = isNullable
        if (!isNullable) {
            defaultValue = newDefaultValue
        }
    }
}

inline fun <reified T> dataClassDecoder(jsonString: String): T? {
    if (jsonString.isEmpty()) {
        return null
    } else {
        return try {
            val dataFields = mutableListOf<Field>()
            val removeFields = mutableListOf<Field>()
            val jsonFields = JsonParser().parse(jsonString).asJsonObject

            T::class.java.declaredFields.forEach {
                dataFields.add(it)
            }

            T::class.java.fields.forEach {
                removeFields.add(it)
            }

            dataFields.removeAll(removeFields)

            if (dataFields.size != jsonFields.size()) {
                throw Exception()
            }

            dataFields.forEach { field ->
                var found = false

                for (name in jsonFields.keySet()) {
                    if (field.name == name) {
                        found = true
                        break
                    }
                }

                if (!found) {
                    throw Exception()
                }
            }

            Gson().fromJson(jsonString, T::class.java)
        } catch (_: Exception) {
            throw Exception("Navigation requires a valid data class object!")
        }
    }
}

fun dataClassEncoder(dataClass: Any?): String {
    return Gson().toJson(dataClass)
}

fun getBearerToken(): String {
    return SecuredPreferences.bearerToken
}

fun setBearerToken(token: String) {
    SecuredPreferences.bearerToken = "bearer $token"
}

fun firstInstall() {
    if (SecuredPreferences.firstInstall) {
        SecuredPreferences.firstInstall = false
        setBearerToken(BuildConfig.REFRESH_TOKEN)
    }
}