package com.joseph.myapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.joseph.myapp.BuildConfig

@Database(
    entities = [Reddit::class],
    version = BuildConfig.DATABASE_VERSION
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getRedditDao(): RedditDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = instance

            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    BuildConfig.DATABASE_NAME
                ).build()
                instance = newInstance
                return newInstance
            }
        }
    }
}