package com.example.pagination_new.data.retrofit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pagination_new.domain.classesss.FavoriteFilm
import com.example.pagination_new.domain.classesss.film.Doc
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm(film: FavoriteFilm)

    @Query("SELECT * FROM FavoriteFilms")
    suspend fun getFavoriteFilms(): List<FavoriteFilm>

    @Query("SELECT COUNT(*) FROM FavoriteFilms")
    suspend fun getCount(): Int

    @Query("DELETE FROM FavoriteFilms WHERE id = :id")
    suspend fun deleteFilmById(id:Int)

}