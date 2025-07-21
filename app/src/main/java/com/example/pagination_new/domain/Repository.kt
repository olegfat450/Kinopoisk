package com.example.pagination_new.domain

import androidx.paging.PagingData
import com.example.pagination_new.domain.classess.Doc
import com.example.pagination_new.domain.classess.Description
import com.example.pagination_new.domain.classess.genre.Genre_list
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface Repository {

    fun getAllFilms(): Flow<PagingData<Doc>>

    fun searchByTitle(title: String): Flow<PagingData<Doc>>

    fun getFilmsWithPoster(): Flow<PagingData<Doc>>

   suspend fun getGenres(): List<Genre_list>

    fun getFilmsByGenre(genre: String): Flow<PagingData<Doc>>

    fun getFilmsByGenreWithPoster(genre: String): Flow<PagingData<Doc>>

    suspend fun getFilmById(id: Int): Response<Description>?
}