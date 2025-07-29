package com.example.pagination_new.domain.useCases

import com.example.pagination_new.domain.Repository

class GetActorByIdUseCase(val repository: Repository) {

    suspend fun execute(id: Int) = repository.getActorById(id)
}