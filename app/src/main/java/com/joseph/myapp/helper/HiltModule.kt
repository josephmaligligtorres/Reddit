package com.joseph.myapp.helper

import android.content.Context
import com.joseph.myapp.BuildConfig
import com.joseph.myapp.data.local.AppDatabase
import com.joseph.myapp.data.local.RedditDao
import com.joseph.myapp.data.remote.api.endpoint.AuthApi
import com.joseph.myapp.data.remote.api.endpoint.DataApi
import com.joseph.myapp.repository.RedditLocalDataSource
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
import okhttp3.Credentials
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {
    @Singleton
    @Provides
    fun providesContext(@ApplicationContext context: Context): WeakReference<Context> {
        return WeakReference(context)
    }

    @Singleton
    @Provides
    fun providesAuthApi(): AuthApi {
        return createApi(
            okHttpClient = createHttpClient(
                token = { Credentials.basic(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET) },
                builder = createAuthApiHttpBuilder()
            ),
            factory = RxJava2CallAdapterFactory.create(),
            baseUrl = BuildConfig.BASE_AUTH_API_URL
        )
    }

    @Singleton
    @Provides
    fun providesDataApi(): DataApi {
        return createApi(
            okHttpClient = createHttpClient(
                token = { getBearerToken() },
                builder = createDataApiHttpBuilder()
            ),
            factory = RxJava2CallAdapterFactory.create(),
            baseUrl = BuildConfig.BASE_DATA_API_URL
        )
    }

    @Singleton
    @Provides
    fun providesRedditRepository(redditDao: RedditDao, context: WeakReference<Context>, api: DataApi): RedditRepository {
        return RedditRepositoryImpl(
            remoteDataSource = RedditRemoteDataSource(
                redditDao = redditDao,
                context = context,
                api = api
            ),
            localDataSource = RedditLocalDataSource(
                redditDao = redditDao
            )
        )
    }

    @Singleton
    @Provides
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun providesRedditDao(appDatabase: AppDatabase): RedditDao {
        return appDatabase.getRedditDao()
    }
}