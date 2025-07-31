package com.example.pagination_new.domain.useCases

import androidx.paging.PagingData
import com.example.pagination_new.domain.classesss.PagerAdapterClass

import com.example.pagination_new.domain.Repository
import kotlinx.coroutines.flow.Flow

class SearchByTitleUseCase(val repository: Repository) {
    fun execute(title: String): Flow<PagingData<PagerAdapterClass>> {
       return repository.searchFilmsByTitle(title)

    }
}