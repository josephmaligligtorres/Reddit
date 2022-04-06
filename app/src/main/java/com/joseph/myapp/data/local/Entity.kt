package com.joseph.myapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.joseph.myapp.BuildConfig

@Entity(tableName = BuildConfig.DATABASE_TABLE_NAME)
data class Reddit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "unique_id") val uniqueId: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") var description: String
)