package com.example.pagination_new.domain.useCases

import com.example.pagination_new.data.RepositoryImpl
import com.example.pagination_new.domain.Repository

class GetAllFilmsUseCase(val repository: Repository) {
    fun execute() = repository.getAllFilms()
}