package com.example.pagination_new.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.pagination_new.data.retrofit.RetrofitInstance.retrofit
import com.example.pagination_new.domain.classess.Doc
import com.example.pagination_new.domain.Repository
import com.example.pagination_new.domain.classess.Description
import com.example.pagination_new.domain.classess.genre.Genre_list
import com.example.pagination_new.domain.person.Docs
import com.example.pagination_new.domain.person.Profession
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class RepositoryImpl: Repository {

    val pageSize = 10
    val initialSize = 30
    val maxSize = 30

    override fun getAllFilms(): Flow<PagingData<Doc>> {
        return Pager(getAllFilmsEvent())
    }

    override fun searchByTitle(title: String): Flow<PagingData<Doc>> {
        return Pager(getFilmByTitleEvent(title))

    }

    override fun getFilmsWithPoster(): Flow<PagingData<Doc>> {
        return Pager(getFilmsWithPosterEvent())

    }
    override suspend fun getGenres(): List<Genre_list> {
        return retrofit.getGenres()
    }

    override fun getFilmsByGenre(genre: String): Flow<PagingData<Doc>> {
       return Pager(getFilmsByGenreEvent(genre))
    }

    override fun getFilmsByGenreWithPoster(genre: String): Flow<PagingData<Doc>> {
        return Pager(getFilmsByGenreWithPosterEvent(genre))
    }

    override fun getFilmsByProfession(profession: String,id: Int): Flow<PagingData<Doc>> {
        return Pager(getFilmsByProfessionEvent(profession,id))
    }

    override suspend fun getIdByName(name: String) = retrofit.getIdByName(name).docs.get(0).id

    override suspend fun getFilmById(id: Int): Response<Description>? {
        return retrofit.getFilmById(id)
    }


    fun Pager(event: Event) = Pager(
        PagingConfig(
            pageSize = pageSize,
            initialLoadSize = initialSize,
            maxSize = maxSize
        )
    ) { PageSource(event) }.flow






}