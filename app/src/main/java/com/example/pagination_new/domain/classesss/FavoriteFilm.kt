package com.example.pagination_new.domain.classesss

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pagination_new.domain.classesss.film.Country
import com.example.pagination_new.domain.classesss.film.Genre
import com.example.pagination_new.domain.classesss.film.Poster
import com.example.pagination_new.domain.classesss.film.Rating

@Entity(tableName = "FavoriteFilms")
data class FavoriteFilm(
      @PrimaryKey(autoGenerate = true)
      val id: Int,
    val countries: String,
    val genres: String,

    val name: String,
    val poster: String,
    val rating: String,

    val year: Int
) {
}