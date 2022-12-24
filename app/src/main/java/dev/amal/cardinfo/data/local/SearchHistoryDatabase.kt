package dev.amal.cardinfo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.amal.cardinfo.data.local.entity.SearchHistoryEntity

@Database(entities = [SearchHistoryEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class SearchHistoryDatabase : RoomDatabase() {
    abstract val dao: SearchHistoryDao
}