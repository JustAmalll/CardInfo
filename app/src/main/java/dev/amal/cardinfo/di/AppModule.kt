package dev.amal.cardinfo.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.amal.cardinfo.common.Constants
import dev.amal.cardinfo.common.Constants.DATABASE_NAME
import dev.amal.cardinfo.data.local.Converters
import dev.amal.cardinfo.data.local.SearchHistoryDatabase
import dev.amal.cardinfo.data.remote.BinListApi
import dev.amal.cardinfo.data.repository.BinListRepositoryImpl
import dev.amal.cardinfo.data.util.GsonParser
import dev.amal.cardinfo.domain.repository.BinListRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBinListApi(): BinListApi = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(BinListApi::class.java)

    @Provides
    @Singleton
    fun provideSearchHistoryDatabase(app: Application): SearchHistoryDatabase =
        Room.databaseBuilder(app, SearchHistoryDatabase::class.java, DATABASE_NAME)
            .addTypeConverter(Converters(GsonParser(Gson())))
            .build()

    @Provides
    @Singleton
    fun provideBinListRepository(
        api: BinListApi, db: SearchHistoryDatabase
    ): BinListRepository = BinListRepositoryImpl(api, db.dao)
}