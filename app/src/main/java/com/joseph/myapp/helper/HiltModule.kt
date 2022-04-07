package com.joseph.myapp.helper

import android.content.Context
import com.joseph.myapp.BuildConfig
import com.joseph.myapp.api.DataApi
import com.joseph.myapp.data.local.AppDatabase
import com.joseph.myapp.data.local.RedditDao
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

    @Provides
    fun providesDataApi(): DataApi {
        return createApi(
            okHttpClient = createHttpClient(
                isAuthApi = false,
                builder = createAuthenticatorHttpBuilder()
            ),
            factory = RxJava2CallAdapterFactory.create(),
            baseUrl = BuildConfig.BASE_DATA_API_URL
        )
    }

    @Provides
    fun providesRedditRepository(
        dataApi: DataApi,
        redditDao: RedditDao,
        context: WeakReference<Context>
    ): RedditRepository {
        return RedditRepositoryImpl(
            remoteDataSource = RedditRemoteDataSource(
                dataApi = dataApi,
                redditDao = redditDao,
                context = context
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
