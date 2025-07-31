package com.example.pagination_new.domain.useCases

import com.example.pagination_new.domain.Repository

class SearchPersonsUseCase(val repository: Repository) {
    fun execute(name: String) = repository.searchPersons(name)
}