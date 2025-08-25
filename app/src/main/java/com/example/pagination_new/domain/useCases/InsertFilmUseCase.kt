package com.example.pagination_new.domain.useCases

import com.example.pagination_new.data.retrofit.MainDb
import com.example.pagination_new.domain.Repository
import com.example.pagination_new.domain.classesss.FavoriteFilm

class InsertFilmUseCase(val repository: Repository) {
    suspend fun execute (db: MainDb,film: FavoriteFilm) = repository.insertFilm(db,film)
}