package com.example.pagination_new.domain.useCases

import androidx.paging.PagingData

import com.example.pagination_new.domain.Repository
import com.example.pagination_new.domain.classess.Doc
import kotlinx.coroutines.flow.Flow

class SearchByTitleUseCase(val repository: Repository) {
    fun execute(title: String): Flow<PagingData<Doc>> {
       return repository.searchByTitle(title)

    }
}