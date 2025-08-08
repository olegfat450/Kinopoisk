package com.example.pagination_new.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.pagination_new.data.retrofit.RetrofitInstance.retrofit
import com.example.pagination_new.domain.classesss.PagerAdapterClass
import com.example.pagination_new.domain.Repository
import com.example.pagination_new.domain.classesss.film.Description
import com.example.pagination_new.domain.classess.genre.Genre_list
import com.example.pagination_new.domain.classesss.person.PersonItem
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class RepositoryImpl: Repository {

    val pageSize = 10
    val initialSize = 30
    val maxSize = 30

    override fun getAllFilms(): Flow<PagingData<PagerAdapterClass>> = Pager(getAllFilmsEvent())


    override fun searchFilmsByTitle(title: String): Flow<PagingData<PagerAdapterClass>> {
        return Pager(getFilmByTitleEvent(title))

    }

    override fun getFilmsWithPoster(): Flow<PagingData<PagerAdapterClass>> {
        return Pager(getFilmsWithPosterEvent())

    }
    override suspend fun getGenres(): List<Genre_list> {
        return retrofit.getGenres()
    }

    override fun getFilmsByGenre(genre: String): Flow<PagingData<PagerAdapterClass>> {
       return Pager(getFilmsByGenreEvent(genre))
    }

    override fun getFilmsByGenreWithPoster(genre: String): Flow<PagingData<PagerAdapterClass>> {
        return Pager(getFilmsByGenreWithPosterEvent(genre))
    }
    override suspend fun getFilmById(id: Int): Response<Description>? {
        return retrofit.getFilmById(id)
    }

    override suspend fun getActorById(id: Int): PersonItem? {
       return retrofit.getPeronById(id).body()
    }

    override fun searchPersons(name: String): Flow<PagingData<PagerAdapterClass>> {
        return Pager(searchPersonsEvent(name))
    }


    fun Pager(event: Event) = Pager(
        PagingConfig(
            pageSize = pageSize,
            initialLoadSize = initialSize,
            maxSize = maxSize
        )
    ) { PageSource(event) }.flow








}