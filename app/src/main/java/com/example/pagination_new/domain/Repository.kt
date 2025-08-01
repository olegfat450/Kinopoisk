package com.example.pagination_new.domain

import androidx.paging.PagingData
import com.example.pagination_new.domain.classesss.film.Description
import com.example.pagination_new.domain.classess.genre.Genre_list
import com.example.pagination_new.domain.classesss.PagerAdapterClass
import com.example.pagination_new.domain.classesss.person.PersonItem
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface Repository {

    fun getAllFilms(): Flow<PagingData<PagerAdapterClass>>

    fun searchFilmsByTitle(title: String): Flow<PagingData<PagerAdapterClass>>

    fun getFilmsWithPoster(): Flow<PagingData<PagerAdapterClass>>

   suspend fun getGenres(): List<Genre_list>

    fun getFilmsByGenre(genre: String): Flow<PagingData<PagerAdapterClass>>

    fun getFilmsByGenreWithPoster(genre: String): Flow<PagingData<PagerAdapterClass>>

//    fun getFilmsByProfession(profession: String,id: Int): Flow<PagingData<PagerAdapterClass>>


//   suspend fun getIdByName(name: String): Int
    suspend fun getFilmById(id: Int): Response<Description>?

    suspend fun getActorById(id: Int): PersonItem?

    fun searchPersons(name: String): Flow<PagingData<PagerAdapterClass>>
}