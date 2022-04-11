package com.joseph.myapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RedditDao {
    @Query("SELECT * FROM tbl_reddit")
    fun getLocalReddits(): Flow<List<Reddit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReddit(reddit: Reddit)

    @Query("DELETE FROM tbl_reddit WHERE unique_id = :uniqueId")
    fun deleteReddit(uniqueId: String)
}