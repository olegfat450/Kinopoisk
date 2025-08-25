package com.example.pagination_new.data.retrofit

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pagination_new.App
import com.example.pagination_new.R
import com.example.pagination_new.domain.classesss.FavoriteFilm
import com.example.pagination_new.domain.classesss.film.Doc
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@Database(
    entities = [FavoriteFilm::class],
    version = 1
)

abstract class MainDb: RoomDatabase() {
    abstract fun dao(): Dao }


