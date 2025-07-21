package com.example.pagination_new.domain.useCases

import com.example.pagination_new.domain.Repository

class GetGenresUseCase(val repository: Repository) {

   suspend fun execute() = repository.getGenres()
}
