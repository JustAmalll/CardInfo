package dev.amal.cardinfo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.amal.cardinfo.data.local.entity.HistoryEntity

@Database(entities = [HistoryEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class HistoryDatabase : RoomDatabase() {
    abstract val dao: HistoryDao
}