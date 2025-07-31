package com.example.pagination_new.domain.useCases

import androidx.paging.PagingData
import com.example.pagination_new.domain.PagerAdapterClass

import com.example.pagination_new.domain.Repository
import kotlinx.coroutines.flow.Flow

class SearchFilmsByTitleUseCase(val repository: Repository) {
    fun execute(title: String): Flow<PagingData<PagerAdapterClass>> {
       return repository.searchByTitle(title)

    }
}