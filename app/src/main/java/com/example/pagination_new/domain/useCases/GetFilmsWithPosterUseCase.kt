package com.example.pagination_new.domain.useCases

import com.example.pagination_new.domain.Repository

class GetFilmsWithPoster (val repository: Repository) {

    fun execute() = repository.getFilmsWithPoster()

}