package com.example.pagination_new.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pagination_new.domain.classesss.PagerAdapterClass
import com.example.pagination_new.domain.useCases.GetAllFilmsUseCase
import com.example.pagination_new.domain.useCases.GetFilmsByGenreUseCase

import com.example.pagination_new.domain.useCases.GetFilmsByGenreWithPosterUseCase

import com.example.pagination_new.domain.useCases.GetFilmsWithPoster

import com.example.pagination_new.domain.useCases.SearchByTitleUseCase
import com.example.pagination_new.domain.useCases.SearchPersonsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

    private val getAllFilmsUseCase: GetAllFilmsUseCase,
                                        private val searchByTitleUseCase: SearchByTitleUseCase,
                                   private val getFilmsByGenreUseCase: GetFilmsByGenreUseCase,
                                   private val getFilmsWithPoster: GetFilmsWithPoster,
                                   private val getFilmsByGenreWithPoster: GetFilmsByGenreWithPosterUseCase,
                                      private val searchPersonsUseCase: SearchPersonsUseCase

                               ): ViewModel() {

   var data: Flow<PagingData<PagerAdapterClass>> = flowOf()



   fun getAllFilms() {data = getAllFilmsUseCase.execute().cachedIn(viewModelScope)}

    fun searchFilmsByTitle (title: String) { data =  searchByTitleUseCase.execute(title) }
    fun getFilmsWithPoster() {data = getFilmsWithPoster.execute().cachedIn(viewModelScope)}
    fun getFilmsByGenre(genre: String) {data = getFilmsByGenreUseCase.execute(genre).cachedIn(viewModelScope)}
    fun getFilmsByGenreWithPoster(genre: String) {data = getFilmsByGenreWithPoster.execute(genre).cachedIn(viewModelScope)}
//    fun getFilmsByProfession(profession: String,id: Int) { data = getFilmsByProfession.execute(profession,id).cachedIn(viewModelScope)}
    fun searchPersons(name: String) { data = searchPersonsUseCase.execute(name)}


}

