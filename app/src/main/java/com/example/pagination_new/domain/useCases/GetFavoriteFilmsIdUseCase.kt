package com.example.pagination_new.domain.useCases

import com.example.pagination_new.data.retrofit.MainDb
import com.example.pagination_new.domain.Repository
import kotlinx.coroutines.CoroutineScope

class GetFavoriteFilmsIdUseCase(val repository: Repository) {
   suspend fun execute(db: MainDb) = repository.getFavoriteFilmsId(db)
}