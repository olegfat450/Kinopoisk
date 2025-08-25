package com.example.pagination_new.domain.useCases

import android.content.Context
import com.example.pagination_new.data.retrofit.MainDb
import com.example.pagination_new.domain.Repository
import kotlinx.coroutines.CoroutineScope

class GetFavoriteFilmsUseCase(val repository: Repository) {

    suspend fun execute(db: MainDb) = repository.getFavoriteFilms(db)
}