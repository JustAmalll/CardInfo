package dev.amal.cardinfo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.amal.cardinfo.data.local.entity.SearchHistoryEntity

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardData(cardData: SearchHistoryEntity)

    @Query("SELECT * FROM searchhistoryentity")
    suspend fun getSearchHistory(): List<SearchHistoryEntity>

    @Query("SELECT * FROM searchhistoryentity WHERE cardBin=:cardBin")
    suspend fun getItemFromSearchHistory(cardBin: String): SearchHistoryEntity

    @Query("DELETE FROM searchhistoryentity WHERE cardBin=:cardBin")
    suspend fun deleteSearchHistoryItem(cardBin: String)

    @Query("DELETE FROM searchhistoryentity")
    suspend fun deleteAllSearchHistory()
}