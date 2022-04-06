package com.joseph.myapp.helper

import android.content.Context
import com.joseph.myapp.BuildConfig
import com.joseph.myapp.api.AuthApi
import com.joseph.myapp.api.DataApi
import com.joseph.myapp.repository.RedditRemoteDataSource
import com.joseph.myapp.repository.RedditRepository
import com.joseph.myapp.repository.RedditRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.lang.ref.WeakReference
import javax.inject.Singleton
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {
    @Singleton
    @Provides
    fun providesContext(
        @ApplicationContext context: Context
    ): WeakReference<Context> {
        return WeakReference(context)
    }

    @Singleton
    @Provides
    fun providesAuthApi(): AuthApi {
        return createApi(
            okHttpClient = createHttpClient(
                isAuthApi = true,
                builder = createHttpBuilder()
            ),
            factory = RxJava2CallAdapterFactory.create(),
            baseUrl = BuildConfig.BASE_AUTH_API_URL
        )
    }

    @Provides
    fun providesDataApi(): DataApi {
        return createApi(
            okHttpClient = createHttpClient(
                isAuthApi = false,
                builder = createHttpBuilder()
            ),
            factory = RxJava2CallAdapterFactory.create(),
            baseUrl = BuildConfig.BASE_DATA_API_URL
        )
    }

    @Provides
    fun providesRedditRepository(
        authApi: AuthApi,
        dataApi: DataApi,
        context: WeakReference<Context>
    ): RedditRepository {
        return RedditRepositoryImpl(
            remoteDataSource = RedditRemoteDataSource(
                authApi = authApi,
                dataApi = dataApi,
                context = context
            )
        )
    }
}
