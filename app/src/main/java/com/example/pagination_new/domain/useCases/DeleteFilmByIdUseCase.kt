package com.example.pagination_new.domain.useCases

import com.example.pagination_new.data.retrofit.MainDb
import com.example.pagination_new.domain.Repository

class DeleteFilmByIdUseCase (val repository: Repository) {

    suspend fun execute(db: MainDb,id: Int) = repository.deleteFilmById(db,id)
}