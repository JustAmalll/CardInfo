package dev.amal.cardinfo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.amal.cardinfo.data.local.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardData(cardData: HistoryEntity)

    @Query("SELECT * FROM historyentity")
    suspend fun getHistory(): List<HistoryEntity>

    @Query("SELECT * FROM historyentity WHERE cardBIN=:cardBIN")
    suspend fun getHistoryItemByCardBIN(cardBIN: String): HistoryEntity

    @Query("DELETE FROM historyentity WHERE cardBIN=:cardBIN")
    suspend fun deleteHistoryItemByCardBIN(cardBIN: String)

    @Query("DELETE FROM historyentity")
    suspend fun deleteAllHistory()
}