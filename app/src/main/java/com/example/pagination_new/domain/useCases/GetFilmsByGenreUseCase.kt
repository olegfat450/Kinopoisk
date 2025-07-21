package com.example.pagination_new.domain.useCases

import com.example.pagination_new.domain.Repository

class GetFilmsByGenreUseCase(val repository: Repository) {

    fun execute(genre: String) = repository.getFilmsByGenre(genre)
}