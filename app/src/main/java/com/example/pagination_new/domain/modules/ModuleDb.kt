package com.example.pagination_new.domain.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.pagination_new.data.retrofit.MainDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleDb {

    @Provides
    @Singleton
    fun provideMainDb(app: Application): MainDb {
        return Room.databaseBuilder(
           app,
            MainDb::class.java,
            "favoriteFilms.db"
        ).build()
    }
}