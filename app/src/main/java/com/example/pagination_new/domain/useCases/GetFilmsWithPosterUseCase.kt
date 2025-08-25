package com.example.pagination_new.domain.useCases

import com.example.pagination_new.domain.Repository

class GetFilmsWithPosterUseCase (val repository: Repository) {

    fun execute() = repository.getFilmsWithPoster()

}