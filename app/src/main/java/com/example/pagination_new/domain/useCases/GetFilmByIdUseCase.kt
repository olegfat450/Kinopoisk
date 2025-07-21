package com.example.pagination_new.domain.useCases

import com.example.pagination_new.domain.Repository

class GetFilmByIdUseCase(val repository: Repository) {

    suspend fun execute(id: Int) = repository.getFilmById(id)
}