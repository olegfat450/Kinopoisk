package com.example.pagination_new.domain.useCases

import com.example.pagination_new.domain.Repository

class GetFilmsByGenreWithPosterUseCase(val repository: Repository) {
    fun execute(genre: String) = repository.getFilmsByGenreWithPoster(genre = genre )
}