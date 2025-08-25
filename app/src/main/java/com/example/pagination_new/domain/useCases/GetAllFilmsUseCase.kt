package com.example.pagination_new.domain.useCases

import android.util.Log
import androidx.paging.PagingData
import com.example.pagination_new.data.PageSource
import com.example.pagination_new.data.RepositoryImpl
import com.example.pagination_new.domain.Repository
import com.example.pagination_new.domain.classesss.PagerAdapterClass
import kotlinx.coroutines.flow.Flow

class GetAllFilmsUseCase(val repository: Repository) {
    fun execute(): Flow<PagingData<PagerAdapterClass>> {

       return repository.getAllFilms() }
}