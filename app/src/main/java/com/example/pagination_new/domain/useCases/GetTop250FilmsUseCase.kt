package com.example.pagination_new.domain.useCases

import com.example.pagination_new.domain.Repository

class GetTop250FilmsUseCase(val repository: Repository) {
    fun execute() = repository.getTop250Films()
}