package com.example.pagination_new.domain.useCases

import androidx.paging.PagingData
import com.example.pagination_new.domain.Repository
import com.example.pagination_new.domain.classesss.PagerAdapterClass
import kotlinx.coroutines.flow.Flow

class GetAllFilmsUseCase(val repository: Repository) {
    fun execute(): Flow<PagingData<PagerAdapterClass>> {

       return repository.getAllFilms() }
}