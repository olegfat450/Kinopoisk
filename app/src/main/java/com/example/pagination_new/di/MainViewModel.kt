package com.example.pagination_new.di

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pagination_new.data.retrofit.MainDb
import com.example.pagination_new.domain.classesss.FavoriteFilm
import com.example.pagination_new.domain.classesss.PagerAdapterClass
import com.example.pagination_new.domain.useCases.DeleteFilmByIdUseCase
import com.example.pagination_new.domain.useCases.GetAllFilmsUseCase
import com.example.pagination_new.domain.useCases.GetFavoriteFilmsUseCase
import com.example.pagination_new.domain.useCases.GetFilmsByGenreUseCase

import com.example.pagination_new.domain.useCases.GetFilmsByGenreWithPosterUseCase


import com.example.pagination_new.domain.useCases.GetFilmsWithPosterUseCase
import com.example.pagination_new.domain.useCases.GetTop250FilmsUseCase
import com.example.pagination_new.domain.useCases.InsertFilmUseCase

import com.example.pagination_new.domain.useCases.SearchFilmsByTitleUseCase
import com.example.pagination_new.domain.useCases.SearchPersonsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor( @ApplicationContext val context: Context,
                            private val db: MainDb,
                            private val getAllFilmsUseCase: GetAllFilmsUseCase,
                                        private val searchByTitleUseCase: SearchFilmsByTitleUseCase,
                                   private val getFilmsByGenreUseCase: GetFilmsByGenreUseCase,
                                   private val getFilmsWithPosterUseCase: GetFilmsWithPosterUseCase,
                                   private val getFilmsByGenreWithPoster: GetFilmsByGenreWithPosterUseCase,
                                      private val searchPersonsUseCase: SearchPersonsUseCase,
                                      private val getTop250FilmsUseCase: GetTop250FilmsUseCase,
                                    private val getFavoriteFilmsUseCase: GetFavoriteFilmsUseCase,
                                  private val deleteFilmByIdUseCase: DeleteFilmByIdUseCase,
                                    private val insertFilmUseCase: InsertFilmUseCase
                               ): ViewModel() {




   var data: Flow<PagingData<PagerAdapterClass>> = flowOf()
    var onDialog = MutableLiveData<Boolean>(false)



   fun getAllFilms() { data = getAllFilmsUseCase.execute().cachedIn(viewModelScope) }

    fun searchFilmsByTitle (title: String) { data =  searchByTitleUseCase.execute(title) }
    fun getFilmsWithPoster() {data = getFilmsWithPosterUseCase.execute().cachedIn(viewModelScope)}
    fun getFilmsByGenre(genre: String) {data = getFilmsByGenreUseCase.execute(genre).cachedIn(viewModelScope)}
    fun getFilmsByGenreWithPoster(genre: String) {data = getFilmsByGenreWithPoster.execute(genre).cachedIn(viewModelScope)}
    fun searchPersons(name: String) { data = searchPersonsUseCase.execute(name).cachedIn(viewModelScope)}
    fun getTop250Films() { data = getTop250FilmsUseCase.execute().cachedIn(viewModelScope)}


    fun getFavoriteFilms() {


        var favoriteCount: Deferred<Int>


        viewModelScope.launch  { data = getFavoriteFilmsUseCase.execute(db) }

            viewModelScope.launch {


                favoriteCount = async { db.dao().getCount() }

                if (favoriteCount.await() == 0) {
                    onDialog.value = true

                    }

            }



    }





    fun deleteFilmById(id: Int) { viewModelScope.launch { deleteFilmByIdUseCase.execute(db,id) } }

    fun insertFilm(film: FavoriteFilm) { viewModelScope.launch { insertFilmUseCase.execute(db,film) }}

}

