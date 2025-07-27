package com.example.pagination_new.domain.useCases

import com.example.pagination_new.domain.Repository
import com.example.pagination_new.domain.person.Profession

class GetFilmsByProfessionalUseCase(val repository: Repository) {

    fun execute(profession: String,id: Int) = repository.getFilmsByProfession(profession,id)
}