package com.example.pagination_new.domain.useCases

import androidx.paging.PagingData
import com.example.pagination_new.domain.Repository
import com.example.pagination_new.domain.person.Docs
import kotlinx.coroutines.flow.Flow

class GetIdByNameUseCse(val repository: Repository) {
   suspend fun execute(name: String): Int {
        return repository.getIdByName(name)

    }
}